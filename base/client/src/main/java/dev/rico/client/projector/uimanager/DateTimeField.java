package dev.rico.client.projector.uimanager;

import dev.rico.common.projector.mixed.CommonUiHelper;
import dev.rico.common.projector.ui.DateTimeFieldModel;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.sun.javafx.scene.traversal.Direction;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import org.apache.commons.lang3.text.WordUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

import static dev.rico.client.remoting.FXBinder.bind;


public class DateTimeField extends HBox {

   private static DateTimeFormatter[] dateFormatters = {
         DateTimeFormatter.ofPattern("d. MMMM yyyy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("d.MMMMyyyy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("d.MMMyyyy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("d.M.yyyy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("ddMMyyyy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("d.M.yy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("ddMMyy").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
   };

   private static DateTimeFormatter[] timeFormatters = {
         DateTimeFormatter.ofPattern("H:mm").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("H:m").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
         DateTimeFormatter.ofPattern("HHmm").withLocale(Locale.GERMAN).withZone(ZoneId.of("UTC")),
   };

   private final TextField dateField;
   private final TextField timeField;
   private final DateTimeFieldModel model;
   private final TextFormatter<LocalTime> localTimeTextFormatter;
   private final TextFormatter<LocalDate> localDateTextFormatter;

   public DateTimeField(DateTimeFieldModel dateTimeFieldModel) {
      model = Objects.requireNonNull(dateTimeFieldModel);
      setSpacing(6);
      dateField = new TextField();
      dateField.setPrefColumnCount(10);
      localDateTextFormatter = new TextFormatter<>(new StringConverter<LocalDate>() {
         @Override
         public String toString(LocalDate object) {
            if (object == null) {
               return "";
            }
            return dateFormatters[0].format(object);
         }

         @Override
         public LocalDate fromString(String string) {
            TemporalAccessor temporalAccessor = detectFormat(string, Iterators.forArray(dateFormatters));
            LocalDate localDate = null;
            if (temporalAccessor != null) {
               localDate = LocalDate.from(temporalAccessor);
            }
            return localDate;
         }
      });
      dateField.setTextFormatter(localDateTextFormatter);
      timeField = new TextField();
      timeField.setPrefColumnCount(3);
      localTimeTextFormatter = new TextFormatter<>(new StringConverter<LocalTime>() {
         @Override
         public String toString(LocalTime object) {
            if (object == null) {
               return "";
            }
            return timeFormatters[0].format(object);
         }

         @Override
         public LocalTime fromString(String string) {
            TemporalAccessor temporalAccessor = detectFormat(string, Iterators.forArray(timeFormatters));
            LocalTime localTime = null;
            if (temporalAccessor != null) {
               localTime = LocalTime.from(temporalAccessor);
            }
            return localTime;
         }
      });
      timeField.setTextFormatter(localTimeTextFormatter);
      getChildren().addAll(dateField, timeField);

      AutoCompletionTextFieldBinding<String> dateBinding = new AutoCompletionTextFieldBinding<>(dateField, this::findDateSuggestions);
      dateBinding.setDelay(0);
      dateBinding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> dateField.impl_traverse(Direction.NEXT));

      AutoCompletionTextFieldBinding<String> timeBinding = new AutoCompletionTextFieldBinding<>(timeField, this::findTimeSuggestions);
      timeBinding.setDelay(0);
      timeBinding.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> event) -> timeField.impl_traverse(Direction.NEXT));

      AutoCompleteField.enableAutoSelectAll(dateField);
      AutoCompleteField.enableAutoSelectAll(timeField);

      bind(localDateProperty()).bidirectionalTo(model.dateProperty());
      bind(localTimeProperty()).bidirectionalTo(model.timeProperty());

      bind(dateField.promptTextProperty()).to(model.datePromptTextProperty(), value -> value == null ? "ddmmyyyy" : value);
      bind(timeField.promptTextProperty()).to(model.timePromptTextProperty(), value -> value == null ? "hhmm" : value);
      bind(dateField.editableProperty()).to(model.editableProperty(), value -> value == null ? true : value);
      bind(timeField.editableProperty()).to(model.editableProperty(), value -> value == null ? true : value);

      HBox.setHgrow(dateField, Priority.SOMETIMES);
      HBox.setHgrow(timeField, Priority.SOMETIMES);

      CommonUiHelper.subscribe(model.resolutionProperty(), evt -> {
         boolean dateVisible = false;
         boolean timeVisible = false;
         if (model.getResolution() == null || model.getResolution() == DateTimeFieldModel.Resolution.DateTime) {
            dateVisible = true;
            timeVisible = true;
         } else if (model.getResolution() == DateTimeFieldModel.Resolution.Date) {
            dateVisible = true;
         } else if (model.getResolution() == DateTimeFieldModel.Resolution.Time) {
            timeVisible = true;
         }
         dateField.setVisible(dateVisible);
         dateField.setManaged(dateVisible);
         timeField.setVisible(timeVisible);
         timeField.setManaged(timeVisible);
      });
   }

   private ObjectProperty<LocalDate> localDateProperty() {
      return localDateTextFormatter.valueProperty();
   }

   private ObjectProperty<LocalTime> localTimeProperty() {
      return localTimeTextFormatter.valueProperty();
   }

   private Collection<String> findDateSuggestions(AutoCompletionBinding.ISuggestionRequest suggestionRequest) {
      return findSuggestion(suggestionRequest, dateFormatters);
   }

   private Collection<String> findTimeSuggestions(AutoCompletionBinding.ISuggestionRequest suggestionRequest) {
      return findSuggestion(suggestionRequest, timeFormatters);
   }

   private Collection<String> findSuggestion(AutoCompletionBinding.ISuggestionRequest suggestionRequest, DateTimeFormatter[] formatters) {
      ArrayList<String> strings = new ArrayList<>();
      TemporalAccessor newInstant = detectFormat(suggestionRequest.getUserText(), Iterators.forArray(formatters));
      if (newInstant != null) {
         strings.add(formatters[0].format(newInstant));
      }
      return strings;
   }

   private TemporalAccessor detectFormat(String dateString, UnmodifiableIterator<DateTimeFormatter> arrayIterator) {
      dateString = WordUtils.capitalizeFully(dateString).replaceAll(" ", "").replaceAll(" ", "");
      return detectFormatInternal(dateString, arrayIterator);
   }

   private TemporalAccessor detectFormatInternal(String dateString, UnmodifiableIterator<DateTimeFormatter> arrayIterator) {
      if (arrayIterator.hasNext()) {
         try {
            return arrayIterator.next().parse(dateString);
         } catch (Exception e) {
            return detectFormatInternal(dateString, arrayIterator);
         }
      }
      return null;
   }
}

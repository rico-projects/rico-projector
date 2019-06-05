package dev.rico.internal.projector.mixed;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.remoting.collections.ListChangeEventImpl;
import dev.rico.remoting.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

public class CommonUiHelper {
    public static <T> void subscribe(Property<T> property, ValueChangeListener<T> changeListener) {
        changeListener.valueChanged(new ValueChangeEvent<T>() {
            @Override
            public Property<T> getSource() {
                return property;
            }

            @Override
            public T getOldValue() {
                return null;
            }

            @Override
            public T getNewValue() {
                return property.get();
            }
        });
        property.onChanged(changeListener);
    }

    public static <T> void subscribe(ObservableList<T> observableList, ListChangeListener<T> listener) {
        listener.listChanged(new ListChangeEventImpl<>(observableList, 0, observableList.size(), new ArrayList<>()));
        observableList.onChanged(listener);
    }

   public static <T> void subscribeWithOptional(Property<T> property, Consumer<Optional<T>> optionalValue) {
        subscribe(property, evt -> optionalValue.accept(Optional.ofNullable(evt.getNewValue())));
    }


   public static void setProperty(IdentifiableModel model, String property, String value) {
      java.util.Objects.requireNonNull(model);
      java.util.Objects.requireNonNull(property);
      // TODO: Besser die folgende Zeile verwenden, dafür muss DP aber erweitert werden :-(
//        model.getProperties().removeIf(s -> s.startsWith(property + "="));
      for (String keyAndValue : model.getProperties()) {
         if (keyAndValue.startsWith(property + "=")) {
            model.getProperties().remove(keyAndValue);
            break;
         }
      }
      if (value != null) {
         model.getProperties().add(property + "=" + value);
      }
   }

   public static String getProperty(IdentifiableModel model, String property) {
      java.util.Objects.requireNonNull(model);
      java.util.Objects.requireNonNull(property);
      String result = model.getProperties().stream().filter(s -> s.startsWith(property + "="))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find property: " + property));
      if (result.length() >= property.length() + 1) {
         result = result.substring(property.length() + 1);
      }
      return result;
   }

   public static boolean hasProperty(IdentifiableModel model, String property) {
      java.util.Objects.requireNonNull(model);
      java.util.Objects.requireNonNull(property);
      String result = model.getProperties().stream().filter(s -> s.startsWith(property + "="))
            .findFirst().orElse(null);
      return result != null;
   }
}

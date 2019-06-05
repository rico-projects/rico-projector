package to.remove;

import com.github.rodionmoiseev.c10n.annotations.De;
import dev.rico.internal.projector.ForRemoval;

@ForRemoval
public interface DocumentTemplateListC10n {
    @De("Eine Vorlage hinterlegen...")
    String addTemplate();
    @De("Vorlage importieren...")
    String importTemplate();
    @De("Vorlage überschreiben...")
    String overwriteTemplate();
    @De("Die Vorlage kann nicht eingelesen werden.")
    String cantReadTemplate();
    @De("Die Vorlage kann nicht importiert werden.")
    String cantImportTemplate();


    @De("Die Dokumentenverwaltung unterstützt nur gezippte sprouts fly Vorlagen.")
    String docAlertI1();
    @De("Die ausgewählte Datei wurde als")
    String docAlertI2();
    @De("erkannt")
    String docAlertI3();

    @De("Die Dokumentenverwaltung unterstützt für den Import nur SFT-Dokumente.")
    String docAlertII1();
    @De("Die ausgewählte Datei wurde als")
    String docAlertII2();
    @De("erkannt")
    String docAlertII3();

    @De("Diese Vorlage ist nicht zum Typ")
    String docAlertIII1();
    @De("kompatibel")
    String docAlertIII2();

    @De("Vorlage entfernen & Standard aktivieren")
    String docAlertIV1();
    @De("Diese Auswahl entfernt Ihre Vorlage und aktiviert\ndie Standard-Vorlage für diesen Dokument-Typ.\n\n" +
            "Falls Sie Ihre Vorlage noch nicht exportiert haben, wird diese unwiderruflich gelöscht!")
    String docAlertIV2();

}

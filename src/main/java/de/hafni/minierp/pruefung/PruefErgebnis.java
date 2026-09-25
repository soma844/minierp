package de.hafni.minierp.pruefung;

public class PruefErgebnis {

    private final String regelId;
    private final PruefStatus status;
    private final String meldung;

    private PruefErgebnis(
            String regelId,
            PruefStatus status,
            String meldung) {

        this.regelId = regelId;
        this.status = status;
        this.meldung = meldung;
    }

    public static PruefErgebnis bestanden(
            String regelId,
            String meldung) {

        return new PruefErgebnis(
                regelId,
                PruefStatus.BESTANDEN,
                meldung);
    }

    public static PruefErgebnis warnung(
            String regelId,
            String meldung) {

        return new PruefErgebnis(
                regelId,
                PruefStatus.WARNUNG,
                meldung);
    }

    public static PruefErgebnis nichtBestanden(
            String regelId,
            String meldung) {

        return new PruefErgebnis(
                regelId,
                PruefStatus.NICHT_BESTANDEN,
                meldung);
    }

    public String getRegelId() {
        return regelId;
    }

    public PruefStatus getStatus() {
        return status;
    }

    public String getMeldung() {
        return meldung;
    }

    public boolean istBestanden() {
        return status == PruefStatus.BESTANDEN;
    }

    public boolean istWarnung() {
        return status == PruefStatus.WARNUNG;
    }

    public boolean istFehler() {
        return status == PruefStatus.NICHT_BESTANDEN;
    }
}
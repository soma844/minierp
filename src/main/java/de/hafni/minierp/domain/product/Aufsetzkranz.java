package de.hafni.minierp.domain.product;

import de.hafni.minierp.exception.DomainValidationException;
import de.hafni.minierp.exception.FehlerCode;

public class Aufsetzkranz {

    private final AufsetzkranzTyp typ;
    private final Material material;
    private final int hoeheMm;
    private final int daemmStaerkeMm;

    public Aufsetzkranz(
            AufsetzkranzTyp typ,
            Material material,
            int hoeheMm,
            int daemmStaerkeMm) {

        if (typ == null) {
            throw new DomainValidationException(
                    FehlerCode.KRANZ_TYP_FEHLT,
                    "Aufsetzkranz-Typ darf nicht null sein.");
        }

        if (material == null) {
            throw new DomainValidationException(
                    FehlerCode.KRANZ_MATERIAL_FEHLT,
                    "Material darf nicht null sein.");
        }

        if (hoeheMm <= 0) {
            throw new DomainValidationException(
                    FehlerCode.KRANZ_HOEHE_UNGUELTIG,
                    "Hoehe muss groesser als 0 sein.");
        }

        if (daemmStaerkeMm < 0) {
            throw new DomainValidationException(
                    FehlerCode.KRANZ_DAEMMUNG_UNGUELTIG,
                    "Daemmstaerke darf nicht negativ sein.");
        }

        this.typ = typ;
        this.material = material;
        this.hoeheMm = hoeheMm;
        this.daemmStaerkeMm = daemmStaerkeMm;
    }

    public AufsetzkranzTyp getTyp() {
        return typ;
    }

    public Material getMaterial() {
        return material;
    }

    public int getHoeheMm() {
        return hoeheMm;
    }

    public int getDaemmStaerkeMm() {
        return daemmStaerkeMm;
    }
}
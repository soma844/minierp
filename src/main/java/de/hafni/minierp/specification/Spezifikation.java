package de.hafni.minierp.specification;

import de.hafni.minierp.pruefung.PruefErgebnis;

public interface Spezifikation<T> {

    PruefErgebnis pruefe(T kandidat);
}
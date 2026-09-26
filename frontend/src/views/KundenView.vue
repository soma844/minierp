<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'

import type {
  Kunde,
  KundeRequest
} from '../types/kunde'

import {
  ladeKunden,
  legeKundeAn,
  aktualisiereKunde,
  loescheKunde
} from '../services/kundeService'

import { ApiError } from '../api/http'


const kunden = ref<Kunde[]>([])
const laden = ref(false)
const fehler = ref<string | null>(null)

const bearbeiten = ref(false)

const urspruenglicheKundenNummer =
  ref<string | null>(null)


const formular = reactive<KundeRequest>({
  kundenNummer: '',
  firmenName: ''
})


async function kundenLaden() {

  laden.value = true
  fehler.value = null

  try {

    kunden.value =
      await ladeKunden()

  } catch (error) {

    fehler.value =
      ermittleFehlertext(error)

  } finally {

    laden.value = false
  }
}


async function speichern() {

  fehler.value = null

  try {

    if (
      bearbeiten.value &&
      urspruenglicheKundenNummer.value !== null
    ) {

      /*
       * Die Kundennummer ist der fachliche Schlüssel.
       *
       * Beim Bearbeiten bleibt sie unverändert.
       * Dadurch sind Pfad und Request garantiert identisch.
       */
      const kundenNummer =
        urspruenglicheKundenNummer.value

      await aktualisiereKunde(
        kundenNummer,
        {
          kundenNummer,
          firmenName:
            formular.firmenName.trim()
        }
      )

    } else {

      await legeKundeAn({
        kundenNummer:
          formular.kundenNummer.trim(),

        firmenName:
          formular.firmenName.trim()
      })
    }

    formularZuruecksetzen()

    await kundenLaden()

  } catch (error) {

    fehler.value =
      ermittleFehlertext(error)
  }
}


function bearbeitenAuswaehlen(
  kunde: Kunde
) {

  /*
   * Die Originalnummer separat merken.
   * Diese wird später für URL UND Request verwendet.
   */
  urspruenglicheKundenNummer.value =
    kunde.kundenNummer

  formular.kundenNummer =
    kunde.kundenNummer

  formular.firmenName =
    kunde.firmenName

  bearbeiten.value = true

  fehler.value = null
}


async function entfernen(
  kunde: Kunde
) {

  const bestaetigt =
    window.confirm(
      `Kunde ${kunde.kundenNummer} wirklich löschen?`
    )

  if (!bestaetigt) {
    return
  }

  fehler.value = null

  try {

    await loescheKunde(
      kunde.kundenNummer
    )

    await kundenLaden()

  } catch (error) {

    fehler.value =
      ermittleFehlertext(error)
  }
}


function formularZuruecksetzen() {

  formular.kundenNummer = ''
  formular.firmenName = ''

  bearbeiten.value = false

  urspruenglicheKundenNummer.value =
    null

  fehler.value = null
}


function ermittleFehlertext(
  error: unknown
): string {

  if (error instanceof ApiError) {

    if (
      typeof error.details === 'object' &&
      error.details !== null
    ) {

      const details =
        error.details as Record<
          string,
          unknown
        >

      if (
        typeof details.detail === 'string'
      ) {
        return details.detail
      }

      if (
        typeof details.title === 'string'
      ) {
        return details.title
      }
    }

    return `HTTP-Fehler ${error.status}`
  }

  if (error instanceof Error) {
    return error.message
  }

  return 'Unbekannter Fehler'
}


onMounted(() => {
  kundenLaden()
})
</script>


<template>

  <section>

    <div class="page-header">

      <h2>
        Kunden
      </h2>

      <p>
        Kundenstammdaten des MiniERP
      </p>

    </div>


    <div
      v-if="fehler"
      class="error-box"
    >
      {{ fehler }}
    </div>


    <div class="card">

      <h3>
        {{
          bearbeiten
            ? 'Kunde bearbeiten'
            : 'Neuen Kunden anlegen'
        }}
      </h3>


      <form
        class="form-grid"
        @submit.prevent="speichern"
      >

        <label>

          Kundennummer

          <input
            v-model="formular.kundenNummer"
            required
            pattern="K-[0-9]{5}"
            placeholder="K-10001"
            title="Format: K-10001 – K- gefolgt von genau 5 Ziffern."
            autocomplete="off"
            :readonly="bearbeiten"
          >

        </label>


        <label>

          Firmenname

          <input
            v-model="formular.firmenName"
            required
            placeholder="Beispiel GmbH"
          >

        </label>


        <div class="form-actions">

          <button
            type="submit"
            class="primary"
          >
            {{
              bearbeiten
                ? 'Speichern'
                : 'Kunde anlegen'
            }}
          </button>


          <button
            v-if="bearbeiten"
            type="button"
            @click="formularZuruecksetzen"
          >
            Abbrechen
          </button>

        </div>

      </form>

    </div>


    <div class="card">

      <div class="table-header">

        <h3>
          Vorhandene Kunden
        </h3>

        <button
          type="button"
          @click="kundenLaden"
        >
          Aktualisieren
        </button>

      </div>


      <p v-if="laden">
        Kunden werden geladen ...
      </p>


      <table
        v-else-if="kunden.length > 0"
      >

        <thead>

          <tr>
            <th>Kundennummer</th>
            <th>Firmenname</th>
            <th>Aktionen</th>
          </tr>

        </thead>


        <tbody>

          <tr
            v-for="kunde in kunden"
            :key="kunde.kundenNummer"
          >

            <td>
              {{ kunde.kundenNummer }}
            </td>

            <td>
              {{ kunde.firmenName }}
            </td>

            <td class="actions">

              <button
                type="button"
                @click="
                  bearbeitenAuswaehlen(
                    kunde
                  )
                "
              >
                Bearbeiten
              </button>


              <button
                type="button"
                class="danger"
                @click="
                  entfernen(kunde)
                "
              >
                Löschen
              </button>

            </td>

          </tr>

        </tbody>

      </table>


      <p v-else>
        Noch keine Kunden vorhanden.
      </p>

    </div>

  </section>

</template>

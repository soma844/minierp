<script setup lang="ts">

import {
  computed,
  onMounted,
  reactive,
  ref
} from 'vue'

import { ApiError } from '../api/http'

import type {
  Auftrag
} from '../types/auftrag'

import type {
  ProduktionsAuftrag,
  ProduktionsAuftragRequest
} from '../types/produktion'

import {
  ladeAuftraege
} from '../services/auftragService'

import {
  ladeProduktionsAuftraege,
  legeProduktionsAuftragAn,
  markiereProduziert,
  schliesseProduktionAb,
  starteProduktion
} from '../services/produktionService'


const auftraege =
  ref<Auftrag[]>([])

const produktionsAuftraege =
  ref<ProduktionsAuftrag[]>([])

const laden =
  ref(false)

const fehler =
  ref<string | null>(null)

const meldung =
  ref<string | null>(null)


const formular =
  reactive<ProduktionsAuftragRequest>({

    produktionsNummer: '',

    auftragsNummer: '',

    geplanterStart:
      new Date()
        .toISOString()
        .substring(0, 10)
  })


const freigegebeneAuftraege =
  computed(() =>
    auftraege.value.filter(
      auftrag =>
        auftrag.status ===
        'FREIGEGEBEN'
    )
  )


async function datenLaden() {

  laden.value = true
  fehler.value = null

  try {

    const [
      geladeneAuftraege,
      geladeneProduktionsAuftraege
    ] = await Promise.all([

      ladeAuftraege(),

      ladeProduktionsAuftraege()
    ])

    auftraege.value =
      geladeneAuftraege

    produktionsAuftraege.value =
      geladeneProduktionsAuftraege

  } catch (error) {

    fehler.value =
      ermittleFehlertext(error)

  } finally {

    laden.value = false
  }
}


async function anlegen() {

  fehler.value = null
  meldung.value = null

  try {

    const request: ProduktionsAuftragRequest = {
      produktionsNummer:
        formular.produktionsNummer,

      auftragsNummer:
        formular.auftragsNummer,

      geplanterStart:
        formular.geplanterStart
    }

    const gespeichert =
      await legeProduktionsAuftragAn(
        request
      )

    meldung.value =
      `Produktionsauftrag ${gespeichert.produktionsNummer} wurde angelegt.`

    formular.produktionsNummer = ''
    formular.auftragsNummer = ''

    await datenLaden()

  } catch (error) {

    fehler.value =
      ermittleFehlertext(error)
  }
}


async function starten(
  produktionsAuftrag:
    ProduktionsAuftrag
) {

  fehler.value = null
  meldung.value = null

  try {

    await starteProduktion(
      produktionsAuftrag
        .produktionsNummer
    )

    meldung.value =
      `${produktionsAuftrag.produktionsNummer}: Produktion gestartet.`

    await datenLaden()

  } catch (error) {

    fehler.value =
      ermittleFehlertext(error)
  }
}


async function produziert(
  produktionsAuftrag:
    ProduktionsAuftrag
) {

  fehler.value = null
  meldung.value = null

  try {

    await markiereProduziert(
      produktionsAuftrag
        .produktionsNummer
    )

    meldung.value =
      `${produktionsAuftrag.produktionsNummer}: Produktion fertig.`

    await datenLaden()

  } catch (error) {

    fehler.value =
      ermittleFehlertext(error)
  }
}


async function abschliessen(
  produktionsAuftrag:
    ProduktionsAuftrag
) {

  fehler.value = null
  meldung.value = null

  try {

    await schliesseProduktionAb(
      produktionsAuftrag
        .produktionsNummer
    )

    meldung.value =
      `${produktionsAuftrag.produktionsNummer} wurde abgeschlossen.`

    await datenLaden()

  } catch (error) {

    fehler.value =
      ermittleFehlertext(error)
  }
}


function statusText(
  status: string
): string {

  switch (status) {

    case 'GEPLANT':
      return 'Geplant'

    case 'IN_PRODUKTION':
      return 'In Produktion'

    case 'PRODUZIERT':
      return 'Produziert'

    case 'ABGESCHLOSSEN':
      return 'Abgeschlossen'

    default:
      return status
  }
}


function ermittleFehlertext(
  error: unknown
): string {

  if (error instanceof ApiError) {

    if (
      typeof error.details ===
        'object'
      && error.details !== null
    ) {

      const details =
        error.details as Record<
          string,
          unknown
        >

      if (
        typeof details.detail ===
        'string'
      ) {
        return details.detail
      }

      if (
        typeof details.title ===
        'string'
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
  datenLaden()
})

</script>


<template>

  <section>

    <div class="page-header">

      <h2>
        Produktion
      </h2>

      <p>
        Produktionsaufträge planen
        und durch die Fertigung führen
      </p>

    </div>


    <div
      v-if="fehler"
      class="error-box"
    >
      {{ fehler }}
    </div>


    <div
      v-if="meldung"
      class="success-box"
    >
      {{ meldung }}
    </div>


    <!-- ================================= -->
    <!-- PRODUKTIONSAUFTRAG ANLEGEN -->
    <!-- ================================= -->

    <div class="card">

      <h3>
        Produktionsauftrag anlegen
      </h3>


      <form
        class="form-grid"
        @submit.prevent="anlegen"
      >

        <div class="three-columns">

          <label>

            Produktionsnummer

            <input
              v-model="
                formular
                  .produktionsNummer
              "
              required
              placeholder="PROD-10001"
            >

          </label>


          <label>

            Freigegebener Auftrag

            <select
              v-model="
                formular
                  .auftragsNummer
              "
              required
            >

              <option value="">
                Bitte auswählen
              </option>

              <option
                v-for="
                  auftrag
                  in freigegebeneAuftraege
                "
                :key="
                  auftrag
                    .auftragsNummer
                "
                :value="
                  auftrag
                    .auftragsNummer
                "
              >

                {{
                  auftrag
                    .auftragsNummer
                }}

                -

                {{
                  auftrag
                    .firmenName
                }}

              </option>

            </select>

          </label>


          <label>

            Geplanter Start

            <input
              v-model="
                formular
                  .geplanterStart
              "
              type="date"
              required
            >

          </label>

        </div>


        <div class="form-actions">

          <button
            type="submit"
            class="primary"
          >
            Produktionsauftrag anlegen
          </button>

        </div>

      </form>

    </div>


    <!-- ================================= -->
    <!-- PRODUKTIONSLISTE -->
    <!-- ================================= -->

    <div class="card">

      <div class="table-header">

        <h3>
          Produktionsaufträge
        </h3>

        <button
          type="button"
          @click="datenLaden"
        >
          Aktualisieren
        </button>

      </div>


      <p v-if="laden">
        Produktionsaufträge
        werden geladen ...
      </p>


      <table
        v-else-if="
          produktionsAuftraege.length > 0
        "
      >

        <thead>

          <tr>
            <th>
              Produktionsnummer
            </th>

            <th>
              Auftrag
            </th>

            <th>
              Geplanter Start
            </th>

            <th>
              Status
            </th>

            <th>
              Nächster Schritt
            </th>
          </tr>

        </thead>


        <tbody>

          <tr
            v-for="
              produktionsAuftrag
              in produktionsAuftraege
            "
            :key="
              produktionsAuftrag
                .produktionsNummer
            "
          >

            <td>
              {{
                produktionsAuftrag
                  .produktionsNummer
              }}
            </td>


            <td>
              {{
                produktionsAuftrag
                  .auftragsNummer
              }}
            </td>


            <td>
              {{
                produktionsAuftrag
                  .geplanterStart
              }}
            </td>


            <td>

              <span
                class="status-badge"
                :class="
                  `status-${produktionsAuftrag.status}`
                "
              >

                {{
                  statusText(
                    produktionsAuftrag
                      .status
                  )
                }}

              </span>

            </td>


            <td>

              <button
                v-if="
                  produktionsAuftrag
                    .status
                    === 'GEPLANT'
                "
                type="button"
                class="primary"
                @click="
                  starten(
                    produktionsAuftrag
                  )
                "
              >
                Produktion starten
              </button>


              <button
                v-else-if="
                  produktionsAuftrag
                    .status
                    ===
                    'IN_PRODUKTION'
                "
                type="button"
                class="primary"
                @click="
                  produziert(
                    produktionsAuftrag
                  )
                "
              >
                Als produziert markieren
              </button>


              <button
                v-else-if="
                  produktionsAuftrag
                    .status
                    === 'PRODUZIERT'
                "
                type="button"
                class="primary"
                @click="
                  abschliessen(
                    produktionsAuftrag
                  )
                "
              >
                Abschließen
              </button>


              <span
                v-else
              >
                Fertig
              </span>

            </td>

          </tr>

        </tbody>

      </table>


      <p v-else>
        Noch keine Produktionsaufträge
        vorhanden.
      </p>

    </div>

  </section>

</template>
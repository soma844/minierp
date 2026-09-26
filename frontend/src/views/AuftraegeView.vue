<script setup lang="ts">

import {

  computed,

  onMounted,

  reactive,

  ref

} from 'vue'

import { ApiError } from '../api/http'

import type { Kunde } from '../types/kunde'

import type { Konfiguration } from '../types/konfiguration'

import type {

  Auftrag,

  AuftragRequest,

  AuftragsPositionRequest

} from '../types/auftrag'

import {

  ladeKunden

} from '../services/kundeService'

import {

  ladeKonfigurationen

} from '../services/konfigurationService'

import {

  gebeAuftragFrei,

  ladeAuftraege,

  legeAuftragAn

} from '../services/auftragService'

const kunden = ref<Kunde[]>([])

const konfigurationen =

  ref<Konfiguration[]>([])

const auftraege =

  ref<Auftrag[]>([])

const laden = ref(false)

const fehler =

  ref<string | null>(null)

const meldung =

  ref<string | null>(null)

const formular =

  reactive<AuftragRequest>({

    auftragsNummer: '',

    kundenNummer: '',

    positionen: [

      {

        positionsNummer: 10,

        menge: 1,

        einzelPreisHt: 2300,

        rabattProzent: 0,

        konfigurationsNummer: ''

      }

    ]

  })

const freigegebeneKonfigurationen =

  computed(() =>

    konfigurationen.value.filter(

      konfiguration =>

        konfiguration.technischerStatus

        === 'FREIGEGEBEN'

    )

  )

function positionHinzufuegen() {

  const letzte =

    formular.positionen[

      formular.positionen.length - 1

    ]

  const neueNummer =

    letzte

      ? letzte.positionsNummer + 10

      : 10

  const position:

    AuftragsPositionRequest = {

      positionsNummer: neueNummer,

      menge: 1,

      einzelPreisHt: 2300,

      rabattProzent: 0,

      konfigurationsNummer: ''

    }

  formular.positionen.push(

    position

  )

}

function positionEntfernen(

  index: number

) {

  if (

    formular.positionen.length <= 1

  ) {

    return

  }

  formular.positionen.splice(

    index,

    1

  )

}

async function stammdatenLaden() {

  laden.value = true

  fehler.value = null

  try {

    const [

      geladeneKunden,

      geladeneKonfigurationen,

      geladeneAuftraege

    ] = await Promise.all([

      ladeKunden(),

      ladeKonfigurationen(),

      ladeAuftraege()

    ])

    kunden.value =

      geladeneKunden

    konfigurationen.value =

      geladeneKonfigurationen

    auftraege.value =

      geladeneAuftraege

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

    const request: AuftragRequest = {

      auftragsNummer:

        formular.auftragsNummer,

      kundenNummer:

        formular.kundenNummer,

      positionen:

        formular.positionen.map(

          position => ({

            positionsNummer:

              position.positionsNummer,

            menge:

              position.menge,

            einzelPreisHt:

              position.einzelPreisHt,

            rabattProzent:

              position.rabattProzent,

            konfigurationsNummer:

              position.konfigurationsNummer

          })

        )

    }

    const gespeichert =

      await legeAuftragAn(

        request

      )

    meldung.value =

      `Auftrag ${gespeichert.auftragsNummer} wurde angelegt.`

    formular.auftragsNummer = ''

    formular.kundenNummer = ''

    formular.positionen.splice(

      0,

      formular.positionen.length,

      {

        positionsNummer: 10,

        menge: 1,

        einzelPreisHt: 2300,

        rabattProzent: 0,

        konfigurationsNummer: ''

      }

    )

    await stammdatenLaden()

  } catch (error) {

    fehler.value =

      ermittleFehlertext(error)

  }

}

async function freigeben(

  auftrag: Auftrag

) {

  fehler.value = null

  meldung.value = null

  try {

    await gebeAuftragFrei(

      auftrag.auftragsNummer

    )

    meldung.value =

      `Auftrag ${auftrag.auftragsNummer} wurde freigegeben.`

    await stammdatenLaden()

  } catch (error) {

    fehler.value =

      ermittleFehlertext(error)

  }

}

function formatiereGeld(

  wert: number

): string {

  return new Intl.NumberFormat(

    'de-DE',

    {

      style: 'currency',

      currency: 'EUR'

    }

  ).format(wert)

}

function ermittleFehlertext(

  error: unknown

): string {

  if (error instanceof ApiError) {

    if (

      typeof error.details === 'object'

      && error.details !== null

    ) {

      const details =

        error.details as Record<string, unknown>

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

  stammdatenLaden()

})

</script>

<template>

  <section>

    <div class="page-header">

      <h2>

        Aufträge

      </h2>

      <p>

        Kundenauftrag mit technisch

        freigegebenen Konfigurationen

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

    <div class="card">

      <h3>

        Neuen Auftrag anlegen

      </h3>

      <form

        class="form-grid"

        @submit.prevent="anlegen"

      >

        <div class="two-columns">

          <label>

            Auftragsnummer

            <input

              v-model="

                formular.auftragsNummer

              "

              required

  pattern="AUF-[0-9]{5}"

  placeholder="AUF-10001"

  title="Format: AUF-10001 – AUF- gefolgt von genau 5 Ziffern."

  autocomplete="off"

            >

          </label>

          <label>

            Kunde

            <select

              v-model="

                formular.kundenNummer

              "

              required

            >

              <option value="">

                Bitte auswählen

              </option>

              <option

                v-for="kunde in kunden"

                :key="

                  kunde.kundenNummer

                "

                :value="

                  kunde.kundenNummer

                "

              >

                {{ kunde.kundenNummer }}

                -

                {{ kunde.firmenName }}

              </option>

            </select>

          </label>

        </div>

        <div class="technical-section">

          <div class="table-header">

            <h3>

              Positionen

            </h3>

            <button

              type="button"

              @click="

                positionHinzufuegen

              "

            >

              + Position

            </button>

          </div>

          <div

            v-for="

              (position, index)

              in formular.positionen

            "

            :key="index"

            class="drive-card"

          >

            <div class="five-columns">

              <label>

                Position

                <input

                  v-model.number="

                    position

                      .positionsNummer

                  "

                  type="number"

                  min="10"
                step="10"

                  required

                >

              </label>

              <label>

                Konfiguration

                <select

                  v-model="

                    position

                      .konfigurationsNummer

                  "

                  required

                >

                  <option value="">

                    Bitte auswählen

                  </option>

                  <option

                    v-for="

                      konfiguration

                      in freigegebeneKonfigurationen

                    "

                    :key="

                      konfiguration

                        .konfigurationsNummer

                    "

                    :value="

                      konfiguration

                        .konfigurationsNummer

                    "

                  >

                    {{

                      konfiguration

                        .konfigurationsNummer

                    }}

                    -

                    {{

                      konfiguration

                        .funktion

                    }}

                  </option>

                </select>

              </label>

              <label>

                Menge

                <input

                  v-model.number="

                    position.menge

                  "

                  type="number"

                  min="1"
                step="1"

                  required

                >

              </label>

              <label>

                Einzelpreis HT

                <input

                  v-model.number="

                    position

                      .einzelPreisHt

                  "

                  type="number"

                  min="0.01"

                  step="0.01"

                  required

                >

              </label>

              <label>

                Rabatt [%]

                <input

                  v-model.number="

                    position

                      .rabattProzent

                  "

                  type="number"

                  min="0"

                  max="100"

                  step="0.01"

                  required

                >

              </label>

            </div>

            <button

              v-if="

                formular

                  .positionen

                  .length > 1

              "

              type="button"

              class="danger"

              @click="

                positionEntfernen(

                  index

                )

              "

            >

              Position entfernen

            </button>

          </div>

        </div>

        <div class="form-actions">

          <button

            type="submit"

            class="primary"

          >

            Auftrag anlegen

          </button>

        </div>

      </form>

    </div>

    <div class="card">

      <div class="table-header">

        <h3>

          Vorhandene Aufträge

        </h3>

        <button

          type="button"

          @click="

            stammdatenLaden

          "

        >

          Aktualisieren

        </button>

      </div>

      <p v-if="laden">

        Aufträge werden geladen ...

      </p>

      <table

        v-else-if="

          auftraege.length > 0

        "

      >

        <thead>

          <tr>

            <th>Auftrag</th>

            <th>Kunde</th>

            <th>Positionen</th>

            <th>Gesamt HT</th>

            <th>Status</th>

            <th>Aktionen</th>

          </tr>

        </thead>

        <tbody>

          <tr

            v-for="

              auftrag

              in auftraege

            "

            :key="

              auftrag.auftragsNummer

            "

          >

            <td>

              {{

                auftrag

                  .auftragsNummer

              }}

            </td>

            <td>

              {{

                auftrag

                  .kundenNummer

              }}

              <br>

              <small>

                {{

                  auftrag

                    .firmenName

                }}

              </small>

            </td>

            <td>

              {{

                auftrag

                  .positionen

                  .length

              }}

            </td>

            <td>

              {{

                formatiereGeld(

                  auftrag.gesamtHt

                )

              }}

            </td>

            <td>

              <span

                class="status-badge"

              >

                {{

                  auftrag.status

                }}

              </span>

            </td>

            <td>

              <button

                v-if="

                  auftrag.status

                  === 'ENTWURF'

                "

                type="button"

                class="primary"

                @click="

                  freigeben(

                    auftrag

                  )

                "

              >

                Freigeben

              </button>

            </td>

          </tr>

        </tbody>

      </table>

      <p v-else>

        Noch keine Aufträge vorhanden.

      </p>

    </div>

  </section>

</template>

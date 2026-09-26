<script setup lang="ts">

import {

  computed,

  onMounted,

  reactive,

  ref,

  toRaw

} from 'vue'

import { ApiError } from '../api/http'

import type {

  AntriebRequest,

  Konfiguration,

  KonfigurationRequest

} from '../types/konfiguration'

import {

  gebeKonfigurationFrei,

  ladeKonfigurationen,

  legeKonfigurationAn,

  loescheKonfiguration,

  pruefeKonfiguration

} from '../services/konfigurationService'

const konfigurationen =

  ref<Konfiguration[]>([])

const laden = ref(false)

const fehler =

  ref<string | null>(null)

const meldung =

  ref<string | null>(null)

const formular =

  reactive<KonfigurationRequest>({

    konfigurationsNummer: '',

    funktion: 'FEST',

    projekt: {

      breiteMm: 1200,

      laengeMm: 1500,

      dachNeigungGrad: 10

    },

    aufsetzkranz: {

      typ: 'STANDARD',

      material: 'VERZINKTER_STAHL',

      hoeheMm: 400,

      daemmStaerkeMm: 50

    },

    festerRahmen: {

      material: 'ALUMINIUM',

      thermischGetrennt: true

    },

    fuellung: {

      art: 'PCA',

      ausfuehrung: 'OPAL',

      dickeMm: 32,

      ugWert: 1.3,

      lichtTransmissionProzent: 38,

      solarFaktorProzent: 40

    },

    oeffnungsRahmen: null,

    antriebsSystem: null

  })

const istLueftung =

  computed(

    () =>

      formular.funktion === 'LUEFTUNG'

  )

function funktionGeaendert() {

  if (

    formular.funktion === 'LUEFTUNG'

  ) {

    formular.oeffnungsRahmen = {

      material: 'ALUMINIUM',

      thermischGetrennt: true,

      gewichtKg: 18

    }

    formular.antriebsSystem = {

      steuerung: {

        versorgungsSpannung:

          'DC_24_V',

        maximalerAusgangsStromAmpere:

          5

      },

      antriebe: [

        {

          art: 'KETTENANTRIEB',

          versorgungsSpannung:

            'DC_24_V',

          hubMm: 500,

          kraftNewton: 500,

          nennStromAmpere: 1.2

        }

      ]

    }

  } else {

    formular.oeffnungsRahmen = null

    formular.antriebsSystem = null

  }

}

function antriebHinzufuegen() {

  if (!formular.antriebsSystem) {

    return

  }

  const antrieb: AntriebRequest = {

    art: 'KETTENANTRIEB',

    versorgungsSpannung:

      'DC_24_V',

    hubMm: 500,

    kraftNewton: 500,

    nennStromAmpere: 1.2

  }

  formular.antriebsSystem

    .antriebe

    .push(antrieb)

}

function antriebEntfernen(

  index: number

) {

  if (!formular.antriebsSystem) {

    return

  }

  formular.antriebsSystem

    .antriebe

    .splice(index, 1)

}

async function ladenAlle() {

  laden.value = true

  fehler.value = null

  try {

    konfigurationen.value =

      await ladeKonfigurationen()

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

    const request =
      structuredClone(toRaw(formular))

    const gespeichert =
      await legeKonfigurationAn(
        request
      )

    meldung.value =

      `Konfiguration ${gespeichert.konfigurationsNummer} angelegt.`

    formular.konfigurationsNummer = ''

    await ladenAlle()

  } catch (error) {

    fehler.value =

      ermittleFehlertext(error)

  }

}

async function pruefen(

  konfiguration: Konfiguration

) {

  fehler.value = null

  meldung.value = null

  try {

    await pruefeKonfiguration(

      konfiguration

        .konfigurationsNummer

    )

    meldung.value =

      `${konfiguration.konfigurationsNummer} wurde technisch geprüft.`

    await ladenAlle()

  } catch (error) {

    fehler.value =

      ermittleFehlertext(error)

  }

}

async function freigeben(

  konfiguration: Konfiguration

) {

  fehler.value = null

  meldung.value = null

  try {

    await gebeKonfigurationFrei(

      konfiguration

        .konfigurationsNummer

    )

    meldung.value =

      `${konfiguration.konfigurationsNummer} wurde freigegeben.`

    await ladenAlle()

  } catch (error) {

    fehler.value =

      ermittleFehlertext(error)

  }

}

async function entfernen(

  konfiguration: Konfiguration

) {

  const bestaetigt =

    window.confirm(

      `Konfiguration ${konfiguration.konfigurationsNummer} wirklich löschen?`

    )

  if (!bestaetigt) {

    return

  }

  fehler.value = null

  meldung.value = null

  try {

    await loescheKonfiguration(

      konfiguration

        .konfigurationsNummer

    )

    await ladenAlle()

  } catch (error) {

    fehler.value =

      ermittleFehlertext(error)

  }

}

function statusText(

  status: string

): string {

  switch (status) {

    case 'ENTWURF':

      return 'Entwurf'

    case 'IN_PRUEFUNG':

      return 'In Prüfung'

    case 'FREIGABEBEREIT':

      return 'Freigabebereit'

    case 'FREIGEGEBEN':

      return 'Freigegeben'

    case 'GESPERRT':

      return 'Gesperrt'

    default:

      return status

  }

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

        error.details as Record<string, unknown>

      if (typeof details.detail === 'string') {

        return details.detail

      }

      if (typeof details.title === 'string') {

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

  ladenAlle()

})

</script>

<template>

  <section>

    <div class="page-header">

      <h2>

        Lichtkuppel-Konfigurationen

      </h2>

      <p>

        Technische Konfiguration,

        Prüfung und Freigabe

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

        Neue Konfiguration

      </h3>

      <form

        class="form-grid"

        @submit.prevent="anlegen"

      >

        <div class="two-columns">

          <label>

            Konfigurationsnummer

            <input

              v-model="

                formular

                  .konfigurationsNummer

              "

              required
              pattern="LK-[0-9]{5}"
              placeholder="LK-10001"
              title="Format: LK-10001 – LK- gefolgt von genau 5 Ziffern."
              autocomplete="off"
            >

          </label>

          <label>

            Funktion

            <select

              v-model="

                formular.funktion

              "

              @change="

                funktionGeaendert

              "

            >

              <option value="FEST">

                Fest

              </option>

              <option value="LUEFTUNG">

                Lüftung

              </option>

            </select>

          </label>

        </div>

        <div class="two-columns">

          <label>

            Produktcode

            <input

              value="ECOLUX-PREMIUM"

              readonly

            >

          </label>

          <label>

            Produktbezeichnung

            <input

              value="ECOLUX Premium Lichtkuppel"

              readonly

            >

          </label>

        </div>

        <h4>

          Projektanforderungen

        </h4>

        <div class="three-columns">

          <label>

            Breite [mm]

            <input

              v-model.number="

                formular

                  .projekt

                  .breiteMm

              "

              type="number"

              required

            >

          </label>

          <label>

            Länge [mm]

            <input

              v-model.number="

                formular

                  .projekt

                  .laengeMm

              "

              type="number"

              required

            >

          </label>

          <label>

            Dachneigung [°]

            <input

              v-model.number="

                formular

                  .projekt

                  .dachNeigungGrad

              "

              type="number"

              required

            >

          </label>

        </div>

        <h4>

          Aufsetzkranz

        </h4>

        <div class="four-columns">

          <label>

            Typ

            <select

              v-model="

                formular

                  .aufsetzkranz

                  .typ

              "

            >

              <option value="STANDARD">

                Standard

              </option>

            </select>

          </label>

          <label>

            Material

            <select

              v-model="

                formular

                  .aufsetzkranz

                  .material

              "

            >

              <option

                value="VERZINKTER_STAHL"

              >

                Verzinkter Stahl

              </option>

              <option value="ALUMINIUM">

                Aluminium

              </option>

            </select>

          </label>

          <label>

            Höhe [mm]

            <input

              v-model.number="

                formular

                  .aufsetzkranz

                  .hoeheMm

              "

              type="number"

              required

            >

          </label>

          <label>

            Dämmstärke [mm]

            <input

              v-model.number="

                formular

                  .aufsetzkranz

                  .daemmStaerkeMm

              "

              type="number"

              required

            >

          </label>

        </div>

        <h4>

          Fester Rahmen

        </h4>

        <div class="two-columns">

          <label>

            Material

            <select

              v-model="

                formular

                  .festerRahmen

                  .material

              "

            >

              <option value="ALUMINIUM">

                Aluminium

              </option>

              <option value="PVC">

                PVC

              </option>

            </select>

          </label>

          <label

            class="checkbox-label"

          >

            <input

              v-model="

                formular

                  .festerRahmen

                  .thermischGetrennt

              "

              type="checkbox"

            >

            Thermisch getrennt

          </label>

        </div>

        <h4>

          Füllung

        </h4>

        <div class="three-columns">

          <label>

            Art
            <select
              v-model="
                formular
                  .fuellung
                  .art
              "
              required
              title="Material der Lichtkuppelfüllung auswählen."
            >

              <option value="PCA">
                PCA – Polycarbonat
              </option>

              <option value="ALUMINIUM">
                Aluminium
              </option>

              <option value="GLAS">
                Glas
              </option>

            </select>

          </label>

          <label>

            Ausführung
            <select
              v-model="
                formular
                  .fuellung
                  .ausfuehrung
              "
              required
              title="Ausführung der Füllung auswählen."
            >

              <option value="OPAL">
                Opal
              </option>

              <option value="TRANSPARENT">
                Transparent
              </option>

              <option value="WAERMEREDUKTION">
                Wärmereduktion
              </option>

            </select>

          </label>

          <label>

            Dicke [mm]

            <input

              v-model.number="

                formular

                  .fuellung

                  .dickeMm

              "

              type="number"

            >

          </label>

          <label>

            Ug-Wert

            <input

              v-model.number="

                formular

                  .fuellung

                  .ugWert

              "

              type="number"

              step="0.1"

            >

          </label>

          <label>

            Lichttransmission [%]

            <input

              v-model.number="

                formular

                  .fuellung

                  .lichtTransmissionProzent

              "

              type="number"

            >

          </label>

          <label>

            Solarfaktor [%]

            <input

              v-model.number="

                formular

                  .fuellung

                  .solarFaktorProzent

              "

              type="number"

            >

          </label>

        </div>

        <!-- ========================== -->

        <!-- NUR BEI LÜFTUNG -->

        <!-- ========================== -->

        <template v-if="istLueftung">

          <div class="technical-section">

            <h3>

              Öffnungsrahmen

            </h3>

            <div

              v-if="

                formular

                  .oeffnungsRahmen

              "

              class="three-columns"

            >

              <label>

                Material

                <select

                  v-model="

                    formular

                      .oeffnungsRahmen

                      .material

                  "

                >

                  <option

                    value="ALUMINIUM"

                  >

                    Aluminium

                  </option>

                  <option value="PVC">

                    PVC

                  </option>

                </select>

              </label>

              <label>

                Gewicht [kg]

                <input

                  v-model.number="

                    formular

                      .oeffnungsRahmen

                      .gewichtKg

                  "

                  type="number"

                  step="0.1"

                  required

                >

              </label>

              <label

                class="checkbox-label"

              >

                <input

                  v-model="

                    formular

                      .oeffnungsRahmen

                      .thermischGetrennt

                  "

                  type="checkbox"

                >

                Thermisch getrennt

              </label>

            </div>

          </div>

          <div class="technical-section">

            <h3>

              Steuerung

            </h3>

            <div

              v-if="

                formular

                  .antriebsSystem

              "

              class="two-columns"

            >

              <label>

                Versorgungsspannung

                <select

                  v-model="

                    formular

                      .antriebsSystem

                      .steuerung

                      .versorgungsSpannung

                  "

                >

                  <option

                    value="DC_24_V"

                  >

                    24 V DC

                  </option>

                  <option

                    value="AC_230_V"

                  >

                    230 V AC

                  </option>

                </select>

              </label>

              <label>

                Max. Ausgangsstrom [A]

                <input

                  v-model.number="

                    formular

                      .antriebsSystem

                      .steuerung

                      .maximalerAusgangsStromAmpere

                  "

                  type="number"

                  step="0.1"

                  required

                >

              </label>

            </div>

          </div>

          <div

            v-if="

              formular

                .antriebsSystem

            "

            class="technical-section"

          >

            <div class="table-header">

              <h3>

                Antriebe

              </h3>

              <button

                type="button"

                @click="

                  antriebHinzufuegen

                "

              >

                + Antrieb

              </button>

            </div>

            <div

              v-for="

                (

                  antrieb,

                  index

                )

                in formular

                  .antriebsSystem

                  .antriebe

              "

              :key="index"

              class="drive-card"

            >

              <div class="four-columns">

                <label>

                  Antriebsart

                  <select

                    v-model="

                      antrieb.art

                    "

                  >

                    <option

                      value="KETTENANTRIEB"

                    >

                      Kettenantrieb

                    </option>

                    <option

                      value="LINEARANTRIEB"

                    >

                      Linearantrieb

                    </option>

                  </select>

                </label>

                <label>

                  Spannung

                  <select

                    v-model="

                      antrieb

                        .versorgungsSpannung

                    "

                  >

                    <option

                      value="DC_24_V"

                    >

                      24 V DC

                    </option>

                    <option

                      value="AC_230_V"

                    >

                      230 V AC

                    </option>

                  </select>

                </label>

                <label>

                  Hub [mm]

                  <input

                    v-model.number="

                      antrieb.hubMm

                    "

                    type="number"

                    required

                  >

                </label>

                <label>

                  Kraft [N]

                  <input

                    v-model.number="

                      antrieb

                        .kraftNewton

                    "

                    type="number"

                    required

                  >

                </label>

                <label>

                  Nennstrom [A]

                  <input

                    v-model.number="

                      antrieb

                        .nennStromAmpere

                    "

                    type="number"

                    step="0.1"

                    required

                  >

                </label>

              </div>

              <button

                v-if="

                  formular

                    .antriebsSystem

                    .antriebe

                    .length > 1

                "

                type="button"

                class="danger"

                @click="

                  antriebEntfernen(

                    index

                  )

                "

              >

                Antrieb entfernen

              </button>

            </div>

          </div>

        </template>

        <div class="form-actions">

          <button

            type="submit"

            class="primary"

          >

            Konfiguration anlegen

          </button>

        </div>

      </form>

    </div>

    <!-- ========================== -->

    <!-- LISTE -->

    <!-- ========================== -->

    <div class="card">

      <div class="table-header">

        <h3>

          Vorhandene Konfigurationen

        </h3>

        <button

          type="button"

          @click="ladenAlle"

        >

          Aktualisieren

        </button>

      </div>

      <p v-if="laden">

        Konfigurationen werden geladen ...

      </p>

      <table

        v-else-if="

          konfigurationen.length > 0

        "

      >

        <thead>

          <tr>

            <th>Nummer</th>

            <th>Produkt</th>

            <th>Funktion</th>

            <th>Status</th>

            <th>Aktionen</th>

          </tr>

        </thead>

        <tbody>

          <tr

            v-for="

              konfiguration

              in konfigurationen

            "

            :key="

              konfiguration

                .konfigurationsNummer

            "

          >

            <td>

              {{

                konfiguration

                  .konfigurationsNummer

              }}

            </td>

            <td>

              {{

                konfiguration

                  .produktBezeichnung

              }}

            </td>

            <td>

              {{

                konfiguration

                  .funktion

              }}

            </td>

            <td>

              <span

                class="status-badge"

                :class="

                  `status-${konfiguration.technischerStatus}`

                "

              >

                {{

                  statusText(

                    konfiguration

                      .technischerStatus

                  )

                }}

              </span>

            </td>

            <td class="actions">

              <button

                v-if="

                  konfiguration

                    .technischerStatus

                    === 'ENTWURF'

                "

                type="button"

                @click="

                  pruefen(

                    konfiguration

                  )

                "

              >

                Prüfen

              </button>

              <button

                v-if="

                  konfiguration

                    .technischerStatus

                    ===

                    'FREIGABEBEREIT'

                "

                type="button"

                class="primary"

                @click="

                  freigeben(

                    konfiguration

                  )

                "

              >

                Freigeben

              </button>

              <button

                v-if="

                  konfiguration

                    .technischerStatus

                    !==

                    'FREIGEGEBEN'

                "

                type="button"

                class="danger"

                @click="

                  entfernen(

                    konfiguration

                  )

                "

              >

                Löschen

              </button>

            </td>

          </tr>

        </tbody>

      </table>

      <p v-else>

        Noch keine Konfigurationen vorhanden.

      </p>

    </div>

  </section>

</template>

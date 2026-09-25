import {
  beforeEach,
  describe,
  expect,
  it,
  vi
} from 'vitest'

import {
  flushPromises,
  mount
} from '@vue/test-utils'

import KonfigurationenView
  from './KonfigurationenView.vue'

import {
  gebeKonfigurationFrei,
  ladeKonfigurationen,
  legeKonfigurationAn,
  loescheKonfiguration,
  pruefeKonfiguration
} from '../services/konfigurationService'


vi.mock(
  '../services/konfigurationService',
  () => ({
    ladeKonfigurationen: vi.fn(),
    legeKonfigurationAn: vi.fn(),
    pruefeKonfiguration: vi.fn(),
    gebeKonfigurationFrei: vi.fn(),
    loescheKonfiguration: vi.fn()
  })
)


describe(
  'KonfigurationenView',
  () => {

    beforeEach(() => {

      vi.resetAllMocks()

      vi.mocked(
        ladeKonfigurationen
      ).mockResolvedValue([])

      vi.mocked(
        pruefeKonfiguration
      ).mockResolvedValue([])

      vi.mocked(
        gebeKonfigurationFrei
      ).mockResolvedValue({
        konfigurationsNummer:
          'LK-TEST-001',

        produktCode:
          'ECOLUX-PREMIUM-ALU',

        produktBezeichnung:
          'ECOLUX Premium Alu',

        funktion: 'FEST',

        technischerStatus:
          'FREIGEGEBEN'
      })

      vi.mocked(
        legeKonfigurationAn
      ).mockResolvedValue({
        konfigurationsNummer:
          'LK-TEST-001',

        produktCode:
          'ECOLUX-PREMIUM-ALU',

        produktBezeichnung:
          'ECOLUX Premium Alu',

        funktion: 'FEST',

        technischerStatus:
          'ENTWURF'
      })

      vi.mocked(
        loescheKonfiguration
      ).mockResolvedValue()
    })


    it(
      'soll vorhandene Konfigurationen anzeigen',
      async () => {

        vi.mocked(
          ladeKonfigurationen
        ).mockResolvedValue([
          {
            konfigurationsNummer:
              'LK-10001',

            produktCode:
              'ECOLUX-PREMIUM-ALU',

            produktBezeichnung:
              'ECOLUX Premium Alu',

            funktion:
              'FEST',

            technischerStatus:
              'ENTWURF'
          }
        ])

        const wrapper =
          mount(
            KonfigurationenView
          )

        await flushPromises()

        expect(
          wrapper.text()
        ).toContain(
          'LK-10001'
        )

        expect(
          wrapper.text()
        ).toContain(
          'ECOLUX Premium Alu'
        )

        expect(
          wrapper.text()
        ).toContain(
          'Entwurf'
        )
      }
    )


    it(
      'soll bei LUEFTUNG die Antriebstechnik anzeigen',
      async () => {

        const wrapper =
          mount(
            KonfigurationenView
          )

        await flushPromises()

        /*
         * FEST ist der Default.
         *
         * Deshalb dürfen diese
         * Bereiche zunächst nicht
         * sichtbar sein.
         */
        expect(
          wrapper.text()
        ).not.toContain(
          'Öffnungsrahmen'
        )

        expect(
          wrapper.text()
        ).not.toContain(
          'Max. Ausgangsstrom'
        )


        /*
         * Das erste Select im
         * Formular ist die Funktion.
         */
        const funktionSelect =
          wrapper.findAll(
            'select'
          )[0]

        await funktionSelect
          .setValue(
            'LUEFTUNG'
          )

        await flushPromises()


        expect(
          wrapper.text()
        ).toContain(
          'Öffnungsrahmen'
        )

        expect(
          wrapper.text()
        ).toContain(
          'Steuerung'
        )

        expect(
          wrapper.text()
        ).toContain(
          'Antriebe'
        )

        expect(
          wrapper.text()
        ).toContain(
          'Max. Ausgangsstrom'
        )

        expect(
          wrapper.text()
        ).toContain(
          'Kettenantrieb'
        )
      }
    )


    it(
      'soll technische Pruefung ausloesen',
      async () => {

        vi.mocked(
          ladeKonfigurationen
        ).mockResolvedValue([
          {
            konfigurationsNummer:
              'LK-PRUEF-001',

            produktCode:
              'ECOLUX',

            produktBezeichnung:
              'Testprodukt',

            funktion:
              'FEST',

            technischerStatus:
              'ENTWURF'
          }
        ])

        const wrapper =
          mount(
            KonfigurationenView
          )

        await flushPromises()


        const pruefenButton =
          wrapper
            .findAll('button')
            .find(
              button =>
                button.text()
                  === 'Prüfen'
            )

        expect(
          pruefenButton
        ).toBeDefined()


        await pruefenButton!
          .trigger('click')

        await flushPromises()


        expect(
          pruefeKonfiguration
        ).toHaveBeenCalledWith(
          'LK-PRUEF-001'
        )
      }
    )


    it(
      'soll freigabebereite Konfiguration freigeben',
      async () => {

        vi.mocked(
          ladeKonfigurationen
        ).mockResolvedValue([
          {
            konfigurationsNummer:
              'LK-FREI-001',

            produktCode:
              'ECOLUX',

            produktBezeichnung:
              'Testprodukt',

            funktion:
              'FEST',

            technischerStatus:
              'FREIGABEBEREIT'
          }
        ])

        const wrapper =
          mount(
            KonfigurationenView
          )

        await flushPromises()


        const freigebenButton =
          wrapper
            .findAll('button')
            .find(
              button =>
                button.text()
                  === 'Freigeben'
            )

        expect(
          freigebenButton
        ).toBeDefined()


        await freigebenButton!
          .trigger('click')

        await flushPromises()


        expect(
          gebeKonfigurationFrei
        ).toHaveBeenCalledWith(
          'LK-FREI-001'
        )
      }
    )

  }
)
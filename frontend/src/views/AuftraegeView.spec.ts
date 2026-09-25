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

import AuftraegeView
  from './AuftraegeView.vue'

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


vi.mock(
  '../services/kundeService',
  () => ({
    ladeKunden: vi.fn()
  })
)

vi.mock(
  '../services/konfigurationService',
  () => ({
    ladeKonfigurationen: vi.fn()
  })
)

vi.mock(
  '../services/auftragService',
  () => ({
    ladeAuftraege: vi.fn(),
    legeAuftragAn: vi.fn(),
    gebeAuftragFrei: vi.fn()
  })
)


describe('AuftraegeView', () => {

  beforeEach(() => {

    vi.resetAllMocks()

    vi.mocked(ladeKunden)
      .mockResolvedValue([
        {
          kundenNummer: 'K-10001',
          firmenName: 'Dynos GmbH'
        }
      ])

    vi.mocked(ladeKonfigurationen)
      .mockResolvedValue([
        {
          konfigurationsNummer: 'LK-FREI-001',
          produktCode: 'ECOLUX',
          produktBezeichnung: 'ECOLUX Premium',
          funktion: 'FEST',
          technischerStatus: 'FREIGEGEBEN'
        },
        {
          konfigurationsNummer: 'LK-ENTWURF-001',
          produktCode: 'ECOLUX',
          produktBezeichnung: 'ECOLUX Test',
          funktion: 'FEST',
          technischerStatus: 'ENTWURF'
        }
      ])

    vi.mocked(ladeAuftraege)
      .mockResolvedValue([])

    vi.mocked(legeAuftragAn)
      .mockResolvedValue({
        auftragsNummer: 'AUF-10001',
        kundenNummer: 'K-10001',
        firmenName: 'Dynos GmbH',
        status: 'ENTWURF',
        positionen: [
          {
            positionsNummer: 10,
            menge: 3,
            einzelPreisHt: 2300,
            rabattProzent: 10,
            konfigurationsNummer: 'LK-FREI-001',
            bruttoHt: 6900,
            rabattBetrag: 690,
            gesamtHt: 6210
          }
        ],
        gesamtHt: 6210
      })

    vi.mocked(gebeAuftragFrei)
      .mockResolvedValue({
        auftragsNummer: 'AUF-10001',
        kundenNummer: 'K-10001',
        firmenName: 'Dynos GmbH',
        status: 'FREIGEGEBEN',
        positionen: [],
        gesamtHt: 6210
      })
  })


  it(
    'soll nur freigegebene Konfigurationen auswählbar machen',
    async () => {

      const wrapper =
        mount(AuftraegeView)

      await flushPromises()

      expect(
        wrapper.text()
      ).toContain('LK-FREI-001')

      expect(
        wrapper.text()
      ).not.toContain('LK-ENTWURF-001')
    }
  )


  it(
    'soll einen Auftrag mit Position anlegen',
    async () => {

      const wrapper =
        mount(AuftraegeView)

      await flushPromises()

      const inputs =
        wrapper.findAll('input')

      /*
       * 0 = Auftragsnummer
       * 1 = Positionsnummer
       * 2 = Menge
       * 3 = Einzelpreis
       * 4 = Rabatt
       */
      await inputs[0].setValue(
        'AUF-10001'
      )

      const selects =
        wrapper.findAll('select')

      /*
       * 0 = Kunde
       * 1 = Konfiguration
       */
      await selects[0].setValue(
        'K-10001'
      )

      await selects[1].setValue(
        'LK-FREI-001'
      )

      await inputs[1].setValue(10)
      await inputs[2].setValue(3)
      await inputs[3].setValue(2300)
      await inputs[4].setValue(10)

      await wrapper
        .find('form')
        .trigger('submit')

      await flushPromises()

      expect(
        legeAuftragAn
      ).toHaveBeenCalledWith({
        auftragsNummer: 'AUF-10001',
        kundenNummer: 'K-10001',
        positionen: [
          {
            positionsNummer: 10,
            menge: 3,
            einzelPreisHt: 2300,
            rabattProzent: 10,
            konfigurationsNummer:
              'LK-FREI-001'
          }
        ]
      })
    }
  )


  it(
    'soll einen Entwurfsauftrag freigeben',
    async () => {

      vi.mocked(ladeAuftraege)
        .mockResolvedValue([
          {
            auftragsNummer: 'AUF-20001',
            kundenNummer: 'K-10001',
            firmenName: 'Dynos GmbH',
            status: 'ENTWURF',
            positionen: [],
            gesamtHt: 1000
          }
        ])

      const wrapper =
        mount(AuftraegeView)

      await flushPromises()

      const button =
        wrapper
          .findAll('button')
          .find(
            element =>
              element.text()
              === 'Freigeben'
          )

      expect(button)
        .toBeDefined()

      await button!
        .trigger('click')

      await flushPromises()

      expect(
        gebeAuftragFrei
      ).toHaveBeenCalledWith(
        'AUF-20001'
      )
    }
  )

})
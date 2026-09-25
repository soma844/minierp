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

import ProduktionView
  from './ProduktionView.vue'

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


vi.mock(
  '../services/auftragService',
  () => ({
    ladeAuftraege: vi.fn()
  })
)

vi.mock(
  '../services/produktionService',
  () => ({
    ladeProduktionsAuftraege: vi.fn(),
    legeProduktionsAuftragAn: vi.fn(),
    starteProduktion: vi.fn(),
    markiereProduziert: vi.fn(),
    schliesseProduktionAb: vi.fn()
  })
)


describe('ProduktionView', () => {

  beforeEach(() => {

    vi.resetAllMocks()

    vi.mocked(ladeAuftraege)
      .mockResolvedValue([
        {
          auftragsNummer: 'AUF-FREI-001',
          kundenNummer: 'K-10001',
          firmenName: 'Dynos GmbH',
          status: 'FREIGEGEBEN',
          positionen: [],
          gesamtHt: 6210
        },
        {
          auftragsNummer: 'AUF-ENTWURF-001',
          kundenNummer: 'K-10001',
          firmenName: 'Dynos GmbH',
          status: 'ENTWURF',
          positionen: [],
          gesamtHt: 1000
        }
      ])

    vi.mocked(
      ladeProduktionsAuftraege
    ).mockResolvedValue([])

    vi.mocked(
      legeProduktionsAuftragAn
    ).mockResolvedValue({
      produktionsNummer: 'PROD-10001',
      auftragsNummer: 'AUF-FREI-001',
      geplanterStart: '2026-10-01',
      status: 'GEPLANT'
    })

    vi.mocked(
      starteProduktion
    ).mockResolvedValue({
      produktionsNummer: 'PROD-10001',
      auftragsNummer: 'AUF-FREI-001',
      geplanterStart: '2026-10-01',
      status: 'IN_PRODUKTION'
    })

    vi.mocked(
      markiereProduziert
    ).mockResolvedValue({
      produktionsNummer: 'PROD-10001',
      auftragsNummer: 'AUF-FREI-001',
      geplanterStart: '2026-10-01',
      status: 'PRODUZIERT'
    })

    vi.mocked(
      schliesseProduktionAb
    ).mockResolvedValue({
      produktionsNummer: 'PROD-10001',
      auftragsNummer: 'AUF-FREI-001',
      geplanterStart: '2026-10-01',
      status: 'ABGESCHLOSSEN'
    })
  })


  it(
    'soll nur freigegebene Auftraege auswählbar machen',
    async () => {

      const wrapper =
        mount(ProduktionView)

      await flushPromises()

      expect(
        wrapper.text()
      ).toContain(
        'AUF-FREI-001'
      )

      expect(
        wrapper.text()
      ).not.toContain(
        'AUF-ENTWURF-001'
      )
    }
  )


  it(
    'soll einen Produktionsauftrag anlegen',
    async () => {

      const wrapper =
        mount(ProduktionView)

      await flushPromises()

      const inputs =
        wrapper.findAll('input')

      /*
       * 0 = Produktionsnummer
       * 1 = Datum
       */
      await inputs[0].setValue(
        'PROD-10001'
      )

      const select =
        wrapper.find('select')

      await select.setValue(
        'AUF-FREI-001'
      )

      await inputs[1].setValue(
        '2026-10-01'
      )

      await wrapper
        .find('form')
        .trigger('submit')

      await flushPromises()

      expect(
        legeProduktionsAuftragAn
      ).toHaveBeenCalledWith({
        produktionsNummer:
          'PROD-10001',

        auftragsNummer:
          'AUF-FREI-001',

        geplanterStart:
          '2026-10-01'
      })
    }
  )


  it(
    'soll Produktion aus GEPLANT starten',
    async () => {

      vi.mocked(
        ladeProduktionsAuftraege
      ).mockResolvedValue([
        {
          produktionsNummer:
            'PROD-START-001',

          auftragsNummer:
            'AUF-FREI-001',

          geplanterStart:
            '2026-10-01',

          status:
            'GEPLANT'
        }
      ])

      const wrapper =
        mount(ProduktionView)

      await flushPromises()

      const button =
        wrapper
          .findAll('button')
          .find(
            element =>
              element.text()
              ===
              'Produktion starten'
          )

      expect(button)
        .toBeDefined()

      await button!
        .trigger('click')

      await flushPromises()

      expect(
        starteProduktion
      ).toHaveBeenCalledWith(
        'PROD-START-001'
      )
    }
  )


  it(
    'soll IN_PRODUKTION als produziert markieren',
    async () => {

      vi.mocked(
        ladeProduktionsAuftraege
      ).mockResolvedValue([
        {
          produktionsNummer:
            'PROD-LAUFEND-001',

          auftragsNummer:
            'AUF-FREI-001',

          geplanterStart:
            '2026-10-01',

          status:
            'IN_PRODUKTION'
        }
      ])

      const wrapper =
        mount(ProduktionView)

      await flushPromises()

      const button =
        wrapper
          .findAll('button')
          .find(
            element =>
              element.text()
              ===
              'Als produziert markieren'
          )

      expect(button)
        .toBeDefined()

      await button!
        .trigger('click')

      await flushPromises()

      expect(
        markiereProduziert
      ).toHaveBeenCalledWith(
        'PROD-LAUFEND-001'
      )
    }
  )


  it(
    'soll PRODUZIERT abschliessen',
    async () => {

      vi.mocked(
        ladeProduktionsAuftraege
      ).mockResolvedValue([
        {
          produktionsNummer:
            'PROD-FERTIG-001',

          auftragsNummer:
            'AUF-FREI-001',

          geplanterStart:
            '2026-10-01',

          status:
            'PRODUZIERT'
        }
      ])

      const wrapper =
        mount(ProduktionView)

      await flushPromises()

      const button =
        wrapper
          .findAll('button')
          .find(
            element =>
              element.text()
              === 'Abschließen'
          )

      expect(button)
        .toBeDefined()

      await button!
        .trigger('click')

      await flushPromises()

      expect(
        schliesseProduktionAb
      ).toHaveBeenCalledWith(
        'PROD-FERTIG-001'
      )
    }
  )

})
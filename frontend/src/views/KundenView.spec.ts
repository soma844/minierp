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

import KundenView from './KundenView.vue'

import {
  ladeKunden,
  legeKundeAn
} from '../services/kundeService'


vi.mock(
  '../services/kundeService',
  () => ({
    ladeKunden: vi.fn(),
    ladeKunde: vi.fn(),
    legeKundeAn: vi.fn(),
    aktualisiereKunde: vi.fn(),
    loescheKunde: vi.fn()
  })
)


describe('KundenView', () => {

  beforeEach(() => {

    vi.resetAllMocks()

    vi.mocked(ladeKunden)
      .mockResolvedValue([])
  })


  it(
    'soll vorhandene Kunden anzeigen',
    async () => {

      vi.mocked(ladeKunden)
        .mockResolvedValue([
          {
            kundenNummer: 'K-10001',
            firmenName: 'Dynos GmbH'
          }
        ])

      const wrapper =
        mount(KundenView)

      await flushPromises()

      expect(
        wrapper.text()
      ).toContain('K-10001')

      expect(
        wrapper.text()
      ).toContain('Dynos GmbH')
    }
  )


  it(
    'soll einen neuen Kunden anlegen',
    async () => {

      vi.mocked(legeKundeAn)
        .mockResolvedValue({
          kundenNummer: 'K-20001',
          firmenName: 'Test GmbH'
        })

      const wrapper =
        mount(KundenView)

      await flushPromises()

      const inputs =
        wrapper.findAll('input')

      await inputs[0].setValue(
        'K-20001'
      )

      await inputs[1].setValue(
        'Test GmbH'
      )

      await wrapper
        .find('form')
        .trigger('submit')

      await flushPromises()

      expect(
        legeKundeAn
      ).toHaveBeenCalledWith({
        kundenNummer: 'K-20001',
        firmenName: 'Test GmbH'
      })
    }
  )

})
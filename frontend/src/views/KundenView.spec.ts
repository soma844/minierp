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
  legeKundeAn,
  aktualisiereKunde
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


  it(
    'soll einen vorhandenen Kunden bearbeiten',
    async () => {

      vi.mocked(ladeKunden)
        .mockResolvedValue([
          {
            kundenNummer: 'K-10001',
            firmenName: 'Dynos GmbH'
          }
        ])

      vi.mocked(aktualisiereKunde)
        .mockResolvedValue({
          kundenNummer: 'K-10001',
          firmenName: 'Dynos Neu GmbH'
        })

      const wrapper =
        mount(KundenView)

      await flushPromises()


      const bearbeitenButton =
        wrapper
          .findAll('button')
          .find(
            button =>
              button.text() === 'Bearbeiten'
          )

      expect(
        bearbeitenButton
      ).toBeDefined()


      await bearbeitenButton!
        .trigger('click')


      const inputs =
        wrapper.findAll('input')


      /*
       * Die Kundennummer ist beim Bearbeiten
       * ein unveränderlicher fachlicher Schlüssel.
       */
      expect(
        inputs[0].attributes()
      ).toHaveProperty('readonly')


      expect(
        inputs[0].element.value
      ).toBe('K-10001')


      await inputs[1].setValue(
        'Dynos Neu GmbH'
      )


      await wrapper
        .find('form')
        .trigger('submit')


      await flushPromises()


      expect(
        aktualisiereKunde
      ).toHaveBeenCalledTimes(1)


      expect(
        aktualisiereKunde
      ).toHaveBeenCalledWith(
        'K-10001',
        {
          kundenNummer: 'K-10001',
          firmenName: 'Dynos Neu GmbH'
        }
      )
    }
  )


  it(
    'soll beim Bearbeiten keinen neuen Kunden anlegen',
    async () => {

      vi.mocked(ladeKunden)
        .mockResolvedValue([
          {
            kundenNummer: 'K-10001',
            firmenName: 'Dynos GmbH'
          }
        ])

      vi.mocked(aktualisiereKunde)
        .mockResolvedValue({
          kundenNummer: 'K-10001',
          firmenName: 'Dynos Neu GmbH'
        })

      const wrapper =
        mount(KundenView)

      await flushPromises()


      const bearbeitenButton =
        wrapper
          .findAll('button')
          .find(
            button =>
              button.text() === 'Bearbeiten'
          )

      await bearbeitenButton!
        .trigger('click')


      const inputs =
        wrapper.findAll('input')

      await inputs[1].setValue(
        'Dynos Neu GmbH'
      )


      await wrapper
        .find('form')
        .trigger('submit')

      await flushPromises()


      expect(
        legeKundeAn
      ).not.toHaveBeenCalled()

      expect(
        aktualisiereKunde
      ).toHaveBeenCalledTimes(1)
    }
  )

})
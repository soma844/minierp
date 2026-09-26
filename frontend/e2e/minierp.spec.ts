import {
  expect,
  test
} from '@playwright/test'


test(
  'kompletter MiniERP Prozess',
  async ({ page }) => {

	/*
	 * Gültige fachliche Nummern.
	 *
	 * Der GitLab-E2E-Job startet für jeden
	 * Lauf eine frische PostgreSQL-Datenbank,
	 * daher brauchen wir hier keine Zeitstempel.
	 */
	const kunde =
	  'K-90001'

	const konfiguration =
	  'LK-90001'

	const auftrag =
	  'AUF-90001'

	const produktion =
	  'PROD-90001'


    // ========================================
    // 1. KUNDE
    // ========================================

    await page.goto('/kunden')

    await page
      .getByLabel('Kundennummer')
      .fill(kunde)

    await page
      .getByLabel('Firmenname')
      .fill('E2E Test GmbH')

    await page
      .getByRole(
        'button',
        {
          name: 'Kunde anlegen'
        }
      )
      .click()


    const kundenZeile =
      page
        .getByRole('row')
        .filter({
          hasText: kunde
        })

    await expect(
      kundenZeile
    ).toContainText(
      'E2E Test GmbH'
    )


    // ========================================
    // 2. KONFIGURATION
    // ========================================

    await page.goto(
      '/konfigurationen'
    )

    await page
      .getByLabel(
        'Konfigurationsnummer'
      )
      .fill(
        konfiguration
      )

    /*
     * Alle übrigen FEST-Werte haben
     * bereits sinnvolle Defaultwerte.
     */
    await page
      .getByRole(
        'button',
        {
          name:
            'Konfiguration anlegen'
        }
      )
      .click()


    const konfigurationsZeile =
      page
        .getByRole('row')
        .filter({
          hasText:
            konfiguration
        })


    await expect(
      konfigurationsZeile
    ).toContainText(
      'Entwurf'
    )


    // technische Prüfung

    await konfigurationsZeile
      .getByRole(
        'button',
        {
          name: 'Prüfen'
        }
      )
      .click()


    await expect(
      konfigurationsZeile
    ).toContainText(
      'Freigabebereit'
    )


    // technische Freigabe

    await konfigurationsZeile
      .getByRole(
        'button',
        {
          name: 'Freigeben'
        }
      )
      .click()


    await expect(
      konfigurationsZeile
    ).toContainText(
      'Freigegeben'
    )


    // ========================================
    // 3. AUFTRAG
    // ========================================

    await page.goto('/auftraege')


    await page
      .getByLabel(
        'Auftragsnummer'
      )
      .fill(
        auftrag
      )


    await page
      .getByLabel('Kunde')
      .selectOption(
        kunde
      )


    await page
      .getByLabel(
        'Konfiguration'
      )
      .selectOption(
        konfiguration
      )


    await page
      .getByLabel('Menge')
      .fill('3')


    await page
      .getByLabel(
        'Einzelpreis HT'
      )
      .fill('2300')


    await page
      .getByLabel(
        'Rabatt [%]'
      )
      .fill('10')


    await page
      .getByRole(
        'button',
        {
          name:
            'Auftrag anlegen'
        }
      )
      .click()


    const auftragsZeile =
      page
        .getByRole('row')
        .filter({
          hasText: auftrag
        })


    await expect(
      auftragsZeile
    ).toContainText(
      'ENTWURF'
    )


    /*
     * Prüft gleichzeitig unsere
     * Backend-Berechnung:
     *
     * 3 x 2300 = 6900
     * -10 %     = 6210
     */
    await expect(
      auftragsZeile
    ).toContainText(
      /6\.210,00/
    )


    await auftragsZeile
      .getByRole(
        'button',
        {
          name: 'Freigeben'
        }
      )
      .click()


    await expect(
      auftragsZeile
    ).toContainText(
      'FREIGEGEBEN'
    )


    // ========================================
    // 4. PRODUKTION
    // ========================================

    await page.goto(
      '/produktion'
    )


    await page
      .getByLabel(
        'Produktionsnummer'
      )
      .fill(
        produktion
      )


    await page
      .getByLabel(
        'Freigegebener Auftrag'
      )
      .selectOption(
        auftrag
      )


    await page
      .getByLabel(
        'Geplanter Start'
      )
      .fill(
        '2026-10-01'
      )


    await page
      .getByRole(
        'button',
        {
          name:
            'Produktionsauftrag anlegen'
        }
      )
      .click()


    const produktionsZeile =
      page
        .getByRole('row')
        .filter({
          hasText:
            produktion
        })


    await expect(
      produktionsZeile
    ).toContainText(
      'Geplant'
    )


    // ========================================
    // GEPLANT -> IN_PRODUKTION
    // ========================================

    await produktionsZeile
      .getByRole(
        'button',
        {
          name:
            'Produktion starten'
        }
      )
      .click()


    await expect(
      produktionsZeile
    ).toContainText(
      'In Produktion'
    )


    // ========================================
    // IN_PRODUKTION -> PRODUZIERT
    // ========================================

    await produktionsZeile
      .getByRole(
        'button',
        {
          name:
            'Als produziert markieren'
        }
      )
      .click()


    await expect(
      produktionsZeile
    ).toContainText(
      'Produziert'
    )


    // ========================================
    // PRODUZIERT -> ABGESCHLOSSEN
    // ========================================

    await produktionsZeile
      .getByRole(
        'button',
        {
          name:
            'Abschließen'
        }
      )
      .click()


    await expect(
      produktionsZeile
    ).toContainText(
      'Abgeschlossen'
    )

    await expect(
      produktionsZeile
    ).toContainText(
      'Fertig'
    )
  }
)
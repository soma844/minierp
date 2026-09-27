import {
  expect,
  test
} from '@playwright/test'


/**
 * End-to-End-Test des vollständigen MiniERP-Hauptprozesses.
 *
 * Der Test bildet einen realistischen Geschäftsablauf ab:
 *
 * Kunde anlegen
 *   -> technische Konfiguration erstellen und freigeben
 *   -> Auftrag erfassen und freigeben
 *   -> Produktionsauftrag erzeugen
 *   -> Produktion durchführen und abschließen
 *
 * Damit wird nicht nur die Benutzeroberfläche getestet,
 * sondern auch das Zusammenspiel der wichtigsten
 * fachlichen Prozesse zwischen Frontend und Backend.
 */
test(
  'kompletter MiniERP Prozess',
  async ({ page }) => {

    /*
     * Feste fachliche Testnummern.
     *
     * Der GitLab-E2E-Job startet für jeden Testlauf
     * mit einer frischen PostgreSQL-Datenbank.
     * Deshalb sind keine Zeitstempel oder zufälligen
     * Nummern zur Vermeidung von Duplikaten notwendig.
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
    // 1. KUNDE ANLEGEN
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


    /*
     * Kundenzeile anhand der eindeutigen
     * Kundennummer ermitteln.
     */
    const kundenZeile =
      page
        .getByRole('row')
        .filter({
          hasText: kunde
        })


    /*
     * Verifizieren, dass der Kunde nach dem
     * Speichern in der Übersicht erscheint.
     */
    await expect(
      kundenZeile
    ).toContainText(
      'E2E Test GmbH'
    )


    // ========================================
    // 2. KONFIGURATION ERSTELLEN
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
     * Die übrigen technischen Eigenschaften
     * besitzen bereits gültige Defaultwerte.
     * Dadurch konzentriert sich dieser E2E-Test
     * auf den eigentlichen Workflow.
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


    /*
     * Neue Konfigurationen beginnen
     * im Status "Entwurf".
     */
    await expect(
      konfigurationsZeile
    ).toContainText(
      'Entwurf'
    )


    // Technische Prüfung der Konfiguration

    await konfigurationsZeile
      .getByRole(
        'button',
        {
          name: 'Prüfen'
        }
      )
      .click()


    /*
     * Eine technisch gültige Konfiguration
     * muss anschließend freigabebereit sein.
     */
    await expect(
      konfigurationsZeile
    ).toContainText(
      'Freigabebereit'
    )


    // Technische Freigabe

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
    // 3. AUFTRAG ANLEGEN
    // ========================================

    await page.goto('/auftraege')


    await page
      .getByLabel(
        'Auftragsnummer'
      )
      .fill(
        auftrag
      )


    /*
     * Auftrag mit dem zuvor angelegten Kunden
     * und der freigegebenen Konfiguration
     * verknüpfen.
     */
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


    /*
     * Neu angelegte Aufträge befinden sich
     * zunächst im Status ENTWURF.
     */
    await expect(
      auftragsZeile
    ).toContainText(
      'ENTWURF'
    )


    /*
     * Gleichzeitig wird die Preisberechnung
     * des Backends überprüft:
     *
     * 3 x 2.300,00 € = 6.900,00 €
     * - 10 % Rabatt = 6.210,00 €
     *
     * Damit deckt der E2E-Test neben dem
     * UI-Workflow auch eine zentrale
     * fachliche Berechnungsregel ab.
     */
    await expect(
      auftragsZeile
    ).toContainText(
      /6\.210,00/
    )


    // Auftrag für die Produktion freigeben

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
    // 4. PRODUKTIONSAUFTRAG ANLEGEN
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


    /*
     * Für die Produktion darf nur ein
     * zuvor freigegebener Auftrag
     * ausgewählt werden.
     */
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


    /*
     * Ein neu angelegter Produktionsauftrag
     * beginnt im Status "Geplant".
     */
    await expect(
      produktionsZeile
    ).toContainText(
      'Geplant'
    )


    // ========================================
    // 5. PRODUKTIONS-WORKFLOW
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


    /*
     * Nach Abschluss müssen sowohl der
     * Prozessstatus als auch die fachliche
     * Fertigmeldung korrekt dargestellt werden.
     */
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

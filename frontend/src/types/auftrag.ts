export type AuftragsStatus =
  | 'ENTWURF'
  | 'TECHNISCH_GEPRUEFT'
  | 'FREIGEGEBEN'
  | 'IN_PRODUKTION'
  | 'PRODUZIERT'
  | 'GELIEFERT'
  | 'ABGESCHLOSSEN'
  | 'STORNIERT'

export interface AuftragsPositionRequest {
  positionsNummer: number
  menge: number
  einzelPreisHt: number
  rabattProzent: number
  konfigurationsNummer: string
}

export interface AuftragRequest {
  auftragsNummer: string
  kundenNummer: string
  positionen: AuftragsPositionRequest[]
}

export interface AuftragsPosition {
  positionsNummer: number
  menge: number
  einzelPreisHt: number
  rabattProzent: number
  konfigurationsNummer: string

  bruttoHt: number
  rabattBetrag: number
  gesamtHt: number
}

export interface Auftrag {
  auftragsNummer: string
  kundenNummer: string
  firmenName: string
  status: AuftragsStatus

  positionen: AuftragsPosition[]

  gesamtHt: number
}
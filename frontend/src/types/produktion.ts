export type ProduktionsStatus =
  | 'GEPLANT'
  | 'IN_PRODUKTION'
  | 'PRODUZIERT'
  | 'ABGESCHLOSSEN'

export interface ProduktionsAuftragRequest {
  produktionsNummer: string
  auftragsNummer: string
  geplanterStart: string
}

export interface ProduktionsAuftrag {
  produktionsNummer: string
  auftragsNummer: string
  geplanterStart: string
  status: ProduktionsStatus
}
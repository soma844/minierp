export type LichtkuppelFunktion =
  | 'FEST'
  | 'LUEFTUNG'
  | 'RAUCHABZUG'

export type TechnischerStatus =
  | 'ENTWURF'
  | 'IN_PRUEFUNG'
  | 'FREIGABEBEREIT'
  | 'FREIGEGEBEN'
  | 'GESPERRT'

export type Material =
  | 'VERZINKTER_STAHL'
  | 'ALUMINIUM'
  | 'PVC'
  | 'POLYCARBONAT'
  | 'GLAS'

export type VersorgungsSpannung =
  | 'AC_230_V'
  | 'DC_24_V'

export type AntriebsArt =
  | 'KETTENANTRIEB'
  | 'LINEARANTRIEB'

export interface ProjektRequest {
  breiteMm: number
  laengeMm: number
  dachNeigungGrad: number
}

export interface AufsetzkranzRequest {
  typ: string
  material: Material
  hoeheMm: number
  daemmStaerkeMm: number
}

export interface FesterRahmenRequest {
  material: Material
  thermischGetrennt: boolean
}

export interface FuellungRequest {
  art: string
  ausfuehrung: string
  dickeMm: number
  ugWert: number
  lichtTransmissionProzent: number
  solarFaktorProzent: number
}

export interface OeffnungsRahmenRequest {
  material: Material
  thermischGetrennt: boolean
  gewichtKg: number
}

export interface SteuerungRequest {
  versorgungsSpannung: VersorgungsSpannung
  maximalerAusgangsStromAmpere: number
}

export interface AntriebRequest {
  art: AntriebsArt
  versorgungsSpannung: VersorgungsSpannung
  hubMm: number
  kraftNewton: number
  nennStromAmpere: number
}

export interface AntriebsSystemRequest {
  steuerung: SteuerungRequest
  antriebe: AntriebRequest[]
}

export interface KonfigurationRequest {
  konfigurationsNummer: string
  produktCode: string
  produktBezeichnung: string
  funktion: LichtkuppelFunktion

  projekt: ProjektRequest
  aufsetzkranz: AufsetzkranzRequest
  festerRahmen: FesterRahmenRequest
  fuellung: FuellungRequest

  oeffnungsRahmen: OeffnungsRahmenRequest | null
  antriebsSystem: AntriebsSystemRequest | null
}

export interface Konfiguration {
  konfigurationsNummer: string
  produktCode: string
  produktBezeichnung: string
  funktion: LichtkuppelFunktion
  technischerStatus: TechnischerStatus
}
import { apiRequest } from '../api/http'

import type {
  ProduktionsAuftrag,
  ProduktionsAuftragRequest
} from '../types/produktion'

const BASE_URL =
  '/api/produktionsauftraege'

export function ladeProduktionsAuftraege():
  Promise<ProduktionsAuftrag[]> {

  return apiRequest<ProduktionsAuftrag[]>(
    BASE_URL
  )
}

export function legeProduktionsAuftragAn(
  request: ProduktionsAuftragRequest
): Promise<ProduktionsAuftrag> {

  return apiRequest<ProduktionsAuftrag>(
    BASE_URL,
    {
      method: 'POST',
      body: JSON.stringify(request)
    }
  )
}

export function starteProduktion(
  produktionsNummer: string
): Promise<ProduktionsAuftrag> {

  return apiRequest<ProduktionsAuftrag>(
    `${BASE_URL}/${encodeURIComponent(produktionsNummer)}/starten`,
    {
      method: 'POST'
    }
  )
}

export function markiereProduziert(
  produktionsNummer: string
): Promise<ProduktionsAuftrag> {

  return apiRequest<ProduktionsAuftrag>(
    `${BASE_URL}/${encodeURIComponent(produktionsNummer)}/produziert`,
    {
      method: 'POST'
    }
  )
}

export function schliesseProduktionAb(
  produktionsNummer: string
): Promise<ProduktionsAuftrag> {

  return apiRequest<ProduktionsAuftrag>(
    `${BASE_URL}/${encodeURIComponent(produktionsNummer)}/abschliessen`,
    {
      method: 'POST'
    }
  )
}
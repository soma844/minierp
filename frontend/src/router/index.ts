import {
  createRouter,
  createWebHistory
} from 'vue-router'
import ProduktionView from '../views/ProduktionView.vue'
import KundenView from '../views/KundenView.vue'
import KonfigurationenView from '../views/KonfigurationenView.vue'
import AuftraegeView from '../views/AuftraegeView.vue'

const router = createRouter({

  history: createWebHistory(),

  routes: [
    {
      path: '/',
      redirect: '/kunden'
    },

    {
      path: '/kunden',
      name: 'kunden',
      component: KundenView
    },

    {
      path: '/konfigurationen',
      name: 'konfigurationen',
      component: KonfigurationenView
    },

    {
      path: '/auftraege',
      name: 'auftraege',
      component: AuftraegeView
    },
	{
	  path: '/produktion',
	  name: 'produktion',
	  component: ProduktionView
	}
  ]
})

export default router
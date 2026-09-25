<script setup lang="ts">
</script>

<template>

  <div class="app">

    <header class="app-header">

      <div>
        <h1>MiniERP</h1>

        <p>
          Lichtkuppel-Konfiguration
          und Produktion
        </p>
      </div>


      <nav>

        <RouterLink to="/kunden">
          Kunden
        </RouterLink>

        <RouterLink to="/konfigurationen">
          Konfigurationen
        </RouterLink>

       <RouterLink to="/auftraege">
  		  Aufträge
		</RouterLink>

        <RouterLink to="/produktion">
  			Produktion
		</RouterLink>

      </nav>

    </header>


    <main class="app-content">

      <RouterView />

    </main>

  </div>

</template>
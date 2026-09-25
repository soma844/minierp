import { defineConfig } from 'vitest/config'
import vue from '@vitejs/plugin-vue'

export default defineConfig({

  plugins: [
    vue()
  ],

  test: {
    environment: 'jsdom',
    globals: true,

    /*
     * Vitest testet ausschließlich
     * unsere Vue-/Frontend-Tests unter src.
     *
     * Die Playwright-E2E-Tests liegen
     * separat unter e2e/.
     */
    include: [
      'src/**/*.spec.ts'
    ]
  }
})
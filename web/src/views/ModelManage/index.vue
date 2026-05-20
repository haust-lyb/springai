<script setup>
import ModelManageHeader from './components/ModelManageHeader.vue'
import ModelToolbar from './components/ModelToolbar.vue'
import ProviderPanel from './components/ProviderPanel.vue'
import UsageTipCard from './components/UsageTipCard.vue'
import ModelTable from './components/ModelTable.vue'

const providers = [
  { id: 'all', name: '全部平台', count: 12, color: '#1677ff', logo: 'cube', active: true },
  { id: 'openai', name: 'OpenAI', count: 4, color: '#10a37f', logo: 'swirl', online: true },
  { id: 'anthropic', name: 'Anthropic', count: 3, color: '#d9c8ae', logo: 'AI', online: true },
  { id: 'google', name: 'Google', count: 2, color: '#4285f4', logo: 'G', online: true },
  { id: 'ollama', name: 'Ollama', count: 2, color: '#0f172a', logo: 'llama' },
  { id: 'azure', name: 'Azure OpenAI', count: 1, color: '#1685d9', logo: 'A', online: true }
]

const models = [
  { id: 1, name: 'GPT-4o', provider: 'OpenAI', providerLogo: 'swirl', providerColor: '#10a37f', modelId: 'gpt-4o', status: '启用', context: '128K', default: true },
  { id: 2, name: 'GPT-4o mini', provider: 'OpenAI', providerLogo: 'swirl', providerColor: '#10a37f', modelId: 'gpt-4o-mini', status: '启用', context: '128K' },
  { id: 3, name: 'GPT-3.5 Turbo', provider: 'OpenAI', providerLogo: 'swirl', providerColor: '#10a37f', modelId: 'gpt-3.5-turbo', status: '启用', context: '16K' },
  { id: 4, name: 'Claude 3.5 Sonnet', provider: 'Anthropic', providerLogo: 'AI', providerColor: '#d9c8ae', modelId: 'claude-3.5-sonnet', status: '启用', context: '200K' },
  { id: 5, name: 'Claude 3 Haiku', provider: 'Anthropic', providerLogo: 'AI', providerColor: '#d9c8ae', modelId: 'claude-3-haiku', status: '启用', context: '200K' },
  { id: 6, name: 'Gemini 1.5 Pro', provider: 'Google', providerLogo: 'G', providerColor: '#4285f4', modelId: 'gemini-1.5-pro', status: '启用', context: '1M' },
  { id: 7, name: 'Gemini 1.5 Flash', provider: 'Google', providerLogo: 'G', providerColor: '#4285f4', modelId: 'gemini-1.5-flash', status: '启用', context: '1M' },
  { id: 8, name: 'Llama 3 70B', provider: 'Ollama', providerLogo: 'llama', providerColor: '#0f172a', modelId: 'llama3:70b', status: '启用', context: '8K' },
  { id: 9, name: 'Llama 3 8B', provider: 'Ollama', providerLogo: 'llama', providerColor: '#0f172a', modelId: 'llama3:8b', status: '启用', context: '8K' },
  { id: 10, name: 'Azure GPT-4', provider: 'Azure OpenAI', providerLogo: 'A', providerColor: '#1685d9', modelId: 'gpt-4-azure', status: '启用', context: '128K' }
]
</script>

<template>
  <main class="model-manage-page">
    <ModelManageHeader />

    <ModelToolbar />

    <section class="model-content-grid">
      <aside class="model-side-column">
        <ProviderPanel :providers="providers" />
        <UsageTipCard />
      </aside>

      <ModelTable :models="models" />
    </section>
  </main>
</template>

<style scoped>
.model-manage-page {
  --model-primary: #1677ff;
  --model-primary-weak: #eaf2ff;
  --model-text: #0f172a;
  --model-muted: #60708f;
  --model-border: #e4eaf3;
  --model-card: rgba(255, 255, 255, 0.92);
  --model-shadow: 0 18px 55px rgba(35, 74, 122, 0.08);

  height: 100vh;
  overflow: auto;
  padding: 28px 36px 34px;
  color: var(--model-text);
  background:
    radial-gradient(circle at 18% 10%, rgba(22, 119, 255, 0.08), transparent 28%),
    radial-gradient(circle at 88% 0%, rgba(91, 141, 239, 0.1), transparent 30%),
    linear-gradient(135deg, #f9fbff 0%, #f4f7fb 48%, #ffffff 100%);
}

.model-content-grid {
  display: grid;
  grid-template-columns: minmax(260px, 300px) minmax(0, 1fr);
  gap: 18px;
  align-items: start;
}

.model-side-column {
  display: grid;
  gap: 16px;
}

@media (max-width: 1360px) {
  .model-manage-page {
    padding: 22px;
  }

  .model-content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .model-manage-page {
    padding: 16px;
  }
}
</style>

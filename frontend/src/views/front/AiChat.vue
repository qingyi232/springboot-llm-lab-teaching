<template>
  <div class="ai-chat-page">
    <div class="chat-container">
      <div class="chat-header">
        <div class="chat-title">
          <div class="ai-avatar"><el-icon :size="24"><ChatDotSquare /></el-icon></div>
          <div>
            <h3>AI智能助手</h3>
            <span>基于DeepSeek大模型 · 实验教学智能辅导</span>
          </div>
        </div>
        <el-button size="small" @click="clearChat" round><el-icon><Delete /></el-icon>清空对话</el-button>
      </div>

      <div class="chat-messages" ref="messagesRef">
        <div class="welcome-msg" v-if="messages.length === 0">
          <div class="ai-avatar large"><el-icon :size="32"><ChatDotSquare /></el-icon></div>
          <h3>你好！我是实验教学AI助手</h3>
          <p>我可以帮助你解答课程问题、指导实验操作、提供学习建议。试试下面的问题：</p>
          <div class="quick-questions">
            <div class="quick-btn" v-for="q in quickQuestions" :key="q" @click="sendMessage(q)">{{ q }}</div>
          </div>
        </div>

        <template v-for="(msg, idx) in messages" :key="idx">
          <div :class="['message', msg.role]">
            <div class="msg-avatar">
              <el-avatar v-if="msg.role === 'user'" :size="36" :src="userStore.userInfo.touxiang || 'https://api.dicebear.com/7.x/avataaars/svg?seed=user'" />
              <div v-else class="ai-avatar small"><el-icon :size="18"><ChatDotSquare /></el-icon></div>
            </div>
            <div class="msg-content">
              <div class="msg-name">{{ msg.role === 'user' ? '我' : 'AI助手' }}</div>
              <div class="msg-text" v-html="renderMarkdown(msg.content)"></div>
            </div>
          </div>
        </template>

        <div class="message assistant" v-if="streaming">
          <div class="msg-avatar"><div class="ai-avatar small"><el-icon :size="18"><ChatDotSquare /></el-icon></div></div>
          <div class="msg-content">
            <div class="msg-name">AI助手</div>
            <div class="msg-text" v-html="renderMarkdown(streamContent)"></div>
            <div class="typing-indicator"><span></span><span></span><span></span></div>
          </div>
        </div>
      </div>

      <div class="chat-input">
        <el-input
          v-model="inputText"
          placeholder="输入你的问题... (Enter发送)"
          @keyup.enter="sendMessage()"
          :disabled="streaming"
          size="large"
        >
          <template #append>
            <el-button type="primary" @click="sendMessage()" :loading="streaming" :icon="Promotion">发送</el-button>
          </template>
        </el-input>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { useUserStore } from '../../store'
import { Promotion } from '@element-plus/icons-vue'
import { marked } from 'marked'

const userStore = useUserStore()
const messagesRef = ref(null)
const inputText = ref('')
const messages = ref([])
const streaming = ref(false)
const streamContent = ref('')

const quickQuestions = [
  '数据结构中链表和数组有什么区别？',
  '如何预约实验室？',
  '操作系统进程调度算法有哪些？',
  '如何查看作业成绩？'
]

const renderMarkdown = (text) => {
  if (!text) return ''
  return marked.parse(text, { breaks: true })
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

const clearChat = () => {
  messages.value = []
  streamContent.value = ''
}

const sendMessage = async (text) => {
  const question = text || inputText.value.trim()
  if (!question || streaming.value) return

  messages.value.push({ role: 'user', content: question })
  inputText.value = ''
  streaming.value = true
  streamContent.value = ''
  scrollToBottom()

  try {
    const token = localStorage.getItem('token')
    const url = `/api/ai/chat/sse?question=${encodeURIComponent(question)}&token=${token}`
    const eventSource = new EventSource(url)

    eventSource.onmessage = (event) => {
      if (event.data === '[DONE]') {
        eventSource.close()
        messages.value.push({ role: 'assistant', content: streamContent.value })
        streaming.value = false
        streamContent.value = ''
        scrollToBottom()
        return
      }
      try {
        const data = JSON.parse(event.data)
        if (data.content) {
          streamContent.value += data.content
          scrollToBottom()
        }
      } catch (e) {}
    }

    eventSource.onerror = () => {
      eventSource.close()
      if (streamContent.value) {
        messages.value.push({ role: 'assistant', content: streamContent.value })
      } else {
        messages.value.push({ role: 'assistant', content: '抱歉，AI服务暂时不可用，请稍后重试。' })
      }
      streaming.value = false
      streamContent.value = ''
      scrollToBottom()
    }
  } catch (e) {
    messages.value.push({ role: 'assistant', content: '网络连接失败，请检查后重试。' })
    streaming.value = false
  }
}
</script>

<style scoped>
.ai-chat-page { height: calc(100vh - 140px); }
.chat-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 16px;
  box-shadow: var(--shadow-sm);
  overflow: hidden;
}
.chat-header {
  padding: 16px 24px;
  border-bottom: 1px solid var(--border-light);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.chat-title { display: flex; align-items: center; gap: 12px; }
.chat-title h3 { font-size: 16px; font-weight: 600; }
.chat-title span { font-size: 12px; color: var(--text-light); }

.ai-avatar {
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}
.ai-avatar { width: 44px; height: 44px; }
.ai-avatar.large { width: 64px; height: 64px; border-radius: 16px; margin-bottom: 16px; }
.ai-avatar.small { width: 36px; height: 36px; border-radius: 10px; }

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}
.welcome-msg {
  text-align: center;
  padding: 48px 20px;
}
.welcome-msg h3 { font-size: 20px; margin-bottom: 8px; }
.welcome-msg p { font-size: 14px; color: var(--text-secondary); margin-bottom: 24px; }
.quick-questions { display: flex; flex-wrap: wrap; gap: 8px; justify-content: center; }
.quick-btn {
  padding: 8px 16px;
  background: var(--primary-bg);
  color: var(--primary-dark);
  border-radius: 20px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid transparent;
}
.quick-btn:hover { border-color: var(--primary); background: white; }

.message {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}
.message.user { flex-direction: row-reverse; }
.message.user .msg-content { align-items: flex-end; }
.message.user .msg-text {
  background: var(--primary);
  color: white;
  border-radius: 16px 16px 4px 16px;
}
.msg-content { display: flex; flex-direction: column; max-width: 70%; }
.msg-name { font-size: 12px; color: var(--text-light); margin-bottom: 4px; }
.msg-text {
  padding: 12px 16px;
  border-radius: 16px 16px 16px 4px;
  background: var(--bg-main);
  font-size: 14px;
  line-height: 1.7;
}
.msg-text :deep(p) { margin-bottom: 8px; }
.msg-text :deep(p:last-child) { margin-bottom: 0; }
.msg-text :deep(code) { background: rgba(0,0,0,0.06); padding: 2px 6px; border-radius: 4px; font-size: 13px; }
.msg-text :deep(strong) { font-weight: 600; }
.msg-text :deep(ul), .msg-text :deep(ol) { padding-left: 20px; margin-bottom: 8px; }

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 4px 0;
}
.typing-indicator span {
  width: 6px;
  height: 6px;
  background: var(--text-light);
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out;
}
.typing-indicator span:nth-child(1) { animation-delay: 0s; }
.typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
.typing-indicator span:nth-child(3) { animation-delay: 0.4s; }
@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.chat-input {
  padding: 16px 24px;
  border-top: 1px solid var(--border-light);
}
</style>

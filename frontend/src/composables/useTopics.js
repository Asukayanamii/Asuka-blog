import request from "@/utils/request";
import { topics as defaultTopics } from "@/data/topics";
import { ref } from "vue";

const topics = ref([]);

topics.value = defaultTopics;

export function getAllTopics() {
    return request.get('/user/topics/list');
}

export async function loadTopics() {
  try {
    const result = await getAllTopics()
    const data = result?.data ?? result
    if (Array.isArray(data)) {
      topics.value = data
    } else {
      console.warn('loadTopics returned unexpected payload:', result)
    }
    return topics.value
  } catch (error) {
    console.error('loadTopics failed', error)
    return topics.value
  }
}

export { topics };
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
    topics.value = result.data
  } catch (error) {
    console.error('loadTopics failed', error)
  }
}

export { topics };
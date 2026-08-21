import request from "@/utils/request";
import { ref } from "vue";

const topics = ref([]);

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

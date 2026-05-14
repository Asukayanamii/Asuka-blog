import request from "@/utils/request";
import { ref } from "vue";

export async function getArticles(pageNum, pageSize, topicId, title) {
    try {
        const params = new URLSearchParams()
        params.append('pageNum', pageNum)
        params.append('pageSize', pageSize)
        if (topicId != null && topicId !== undefined) {
            params.append('topicId', topicId)
        }
        if (title != null && title !== undefined && title !== '') {
            params.append('title', title)
        }
        
        const result = await request.get('/user/articles/list?' + params.toString())
        return result.data
    }
    catch (error) {        
        console.error('getArticles failed', error)
        return []
    }
}

export async function loadArticles() {
  try {
    const result = await getAllArticles()
    articles.value = result.data
  } catch (error) {
    console.error('loadArticles failed', error)
  }
}
import request from "@/utils/request";

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

export async function getArticleDetail(id) {
  try {
    const result = await request.get('/user/articles/detail?id=' + id)
    return result.data
  } catch (error) {
    console.error('getArticleDetail failed', error)
    return null
  }
}
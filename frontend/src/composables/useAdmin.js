import request from "@/utils/request";

export function adminLogin(username, password) {
    return request.post('/admin/login', { username, password });
}

export function getArticles(pageNum, pageSize, title) {
    const params = new URLSearchParams()
    params.append('pageNum', pageNum)
    params.append('pageSize', pageSize)
    if (title) params.append('title', title)
    return request.get('/admin/articles/list?' + params.toString())
}

export function getArticleDetail(id) {
    return request.get('/admin/articles/' + id)
}

export function createArticle(data) {
    return request.post('/admin/articles', data)
}

export function updateArticle(id, data) {
    return request.put('/admin/articles/' + id, data)
}

export function deleteArticle(id) {
    return request.delete('/admin/articles/' + id)
}

export function getTopics() {
    return request.get('/admin/topics/list')
}

export function getTopicDetail(id) {
    return request.get('/admin/topics/' + id)
}

export function createTopic(data) {
    return request.post('/admin/topics', data)
}

export function updateTopic(id, data) {
    return request.put('/admin/topics/' + id, data)
}

export function deleteTopic(id) {
    return request.delete('/admin/topics/' + id)
}

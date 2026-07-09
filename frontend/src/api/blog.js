import request from '../utils/request'

export function getBlogList() {
  return request.get('/blogs')
}

export function getBlogById(id) {
  return request.get(`/blogs/${id}`)
}

export function getMyBlogs() {
  return request.get('/blogs/my')
}

export function getAdminAllBlogs(username, title) {
  const params = {}
  if (username) params.username = username
  if (title) params.title = title
  return request.get('/blogs/admin/all', { params })
}

export function addBlog(data) {
  return request.post('/blogs', data)
}

export function updateBlog(id, data) {
  return request.put(`/blogs/${id}`, data)
}

export function deleteBlog(id) {
  return request.delete(`/blogs/${id}`)
}
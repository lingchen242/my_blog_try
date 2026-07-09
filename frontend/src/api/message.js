import request from '../utils/request'

export function getMessageList(params) {
  return request.get('/messages', { params })
}

export function addMessage(data) {
  return request.post('/messages', data)
}

export function deleteMessage(id) {
  return request.delete(`/messages/${id}`)
}
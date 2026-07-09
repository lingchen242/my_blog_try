import request from '../utils/request'

export function getUserList() {
  return request.get('/admin/users')
}

export function deleteUser(id) {
  return request.delete(`/admin/users/${id}`)
}
import request from '@/api/request.js'

const listUsers = () => {
  return request({
    url: '/admin/users',
    method: 'get'
  })
}

const pageUsers = (data) => {
  return request({
    url: '/admin/users/page',
    method: 'post',
    data
  })
}

const getStats = () => {
  return request({
    url: '/admin/users/stats',
    method: 'get'
  })
}

const createUser = (data) => {
  return request({
    url: '/admin/users',
    method: 'post',
    data
  })
}

const updateUser = (id, data) => {
  return request({
    url: '/admin/users/update',
    method: 'post',
    params: { id },
    data
  })
}

const resetPassword = (id, password) => {
  return request({
    url: '/admin/users/password',
    method: 'post',
    params: { id },
    data: { password }
  })
}

const deleteUser = (id) => {
  return request({
    url: '/admin/users/delete',
    method: 'post',
    params: { id }
  })
}

export default {
  listUsers,
  pageUsers,
  getStats,
  createUser,
  updateUser,
  resetPassword,
  deleteUser
}

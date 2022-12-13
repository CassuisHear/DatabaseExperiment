function loginApi(data) {
  return $axios({
    'url': '/user/login',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/user/logout',
    'method': 'post',
  })
}

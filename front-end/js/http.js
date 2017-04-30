var $http = (function () {
  var host = 'http://www.linzhongming.com:8080/InvoiceMS'

  function ajax(url, options) {
    if (options && typeof options !== 'object') {
      throw new TypeError('$http.ajax([string], [object])')
    }

    if (!options) {
      options = {}
    }

    if (url.indexOf('http') === 0) {
      options.url = url
    } else {
      options.url = host + url
    }

    return $.ajax(options)
      .then(function (resp) {
        return JSON.parse(resp)
      })
  }

  function get(url, options) {
    if (!options) {
      options = {}
    }

    options.method = 'GET'

    return ajax(url, options)
  }

  function post(url, data, options) {
    if (!options) {
      options = {}
    }

    options.method = 'POST'
    options.data = data

    return ajax(url, options)
  }

  return {
    ajax: ajax,
    get: get,
    post: post
  }
})()
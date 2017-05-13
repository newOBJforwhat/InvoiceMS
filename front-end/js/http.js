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

    options.xhrFields = {withCredentials: true}

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

  function all(reqArr) {
    var reqs = []

    Array.prototype.forEach.call(arguments, function (arg) {
      reqs = reqs.concat(arg)
    })

    return $.when.apply(null, reqArr)
      .then(function () {
        var resps = []

        Array.prototype.forEach.call(arguments, function (arg) {
          resps.push(arg)
        })

        return resps
      })
  }

  return {
    ajax: ajax,
    get: get,
    post: post,
    all: all
  }
})()
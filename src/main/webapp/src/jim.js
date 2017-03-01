(function(global, factory) {
    'use strict';
    if (typeof global.define === 'function' && global.define.amd) {
        global.define('jim', [], factory);
    } else {
        global.jim = factory(global);
    }
})(window, function(global) {
    'use strict';
    function noop() {
        //
    }

    // 日志类型
    var LOG_INFO = 'info',
        LOG_ERROR = 'error';
    // 是否支持websocket
    var supportWS = false;

    var maxConnectNumber = 3;

    /**
     * 即时通信构造函数
     *
     * @param options
     * @returns {boolean}
     * @constructor
     */
    function Jim(options) {
        // websocket的地址
        if (typeof options.wsUrl === 'string' && options.wsUrl.trim() !== '') {
            this.wsUrl = options.wsUrl;
        } else {
            this.log('缺少wsUrl参数', LOG_ERROR);
        }
        if (typeof options.xhrUrl === 'string' && options.xhrUrl.trim() !== '') {
            this.xhrUrl = options.xhrUrl;
        } else {
            this.log('缺少xhrUrl参数', LOG_ERROR);
        }
        this.name = options.name || '(建议加上name属性以区分不同的socket)';
        // 链接打开时的callback
        this.onOpen = typeof options.onOpen === 'function' ? options.onOpen : noop;
        // 链接关闭时的回调函数
        this.onClose = typeof options.onClose === 'function' ? options.onClose: noop;
        // t
        this.onMessage = typeof options.onMessage === 'function' ? options.onMessage: noop;
        this.onError = typeof options.onError === 'function' ? options.onError: noop;
        this.debug = options.debug || false;
        this.isLive = true;
        this.interval = parseInt(options.interval) || 500;
    }
    Jim.prototype = {
        /**
         * 开启链接
         */
        start: function() {
            var that = this;
            if (supportWS) {
                this.socket = new global.WebSocket(that.wsUrl);
                this.socket.onopen = function(e) {
                    that.log('建立链接', LOG_INFO);
                    that.onOpen();
                };
                this.socket.onclose = function(e) {
                    that.log('链接关闭', LOG_INFO);
                    that.onClose();
                };
                this.socket.onmessage = function(e) {
                    that.log('有新消息', LOG_INFO);
                    that.onMessage(e);
                };
                this.socket.onerror = function(e) {
                    that.log('链接发生错误', LOG_ERROR);
                    that.onError();
                };
            } else {
                ajax({
                    url: that.xhrUrl + '/info',
                    method: 'GET',
                    success: function(data) {
                        that.log('已链接', LOG_INFO);
                        that.onOpen();
                        that._get();
                    },
                    error: function() {
                        that.onError();
                        that.log('链接失败', LOG_ERROR);
                    }
                });
            }
        },
        /**
         * 发送消息
         *
         * @param text
         */
        send: function(text) {
            var that = this;
            if (supportWS) {
                this.socket.send(text);
                this.log('发送消息：' + text, LOG_INFO);
            } else {
                ajax({
                    url: that.xhrUrl,
                    method: 'POST',
                    success: function(data) {
                        that.log('发送消息成功', LOG_INFO);
                    },
                    error: function(data) {
                        console.log('发送消息失败', LOG_ERROR);
                    }
                });
            }
        },
        /**
         * 关闭链接
         */
        close: function() {
            if (supportWS) {
                this.socket.close();
            } else {
                this.isLive = false;
            }
        },
        _get: function() {
            var that = this;
            if (!this.isLive) {
                return false;
            }
            if (maxConnectNumber === 0) {
                setTimeout(function () {
                    maxConnectNumber = 3;
                    that._get();
                }, 10000);
            }
            ajax({
                url: that.xhrUrl + '/911/abcdef/xhr',
                method: 'GET',
                success: function(res) {
                    console.log('ajax返回：' + res);
                    if (res == 'h') {
                        return false;
                    }
                    that.onMessage(res);
                },
                callback: function() {
                    that._get();
                },
                error: function() {
                    maxConnectNumber--;
                }
            })
        },
        /**
         * 打印日志
         * @param msg
         * @param errType
         * @returns {boolean}
         */
        log: function(msg, errType) {
            var prefix = this.name + ' - ';
            if (errType !== LOG_ERROR && !this.debug) {
                return false;
            }
            console[console[errType] ? errType : 'log'](prefix + msg);
        }
    };
    function ajax(opt) {
        var xhr = global.XMLHttpRequest ? new global.XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
        if (typeof opt.url !== 'string' && opt.url.trim() === '') {
            alert('ajax请求缺少正确的url参数');
            return false;
        }
        var legalMethods = ['GET', 'POST', 'PUT', 'DELETE', 'PATCH'];
        if (legalMethods.indexOf(opt.method) < 0) {
            alert('ajax方法的method参数错误, 应为如下方法之一: ' + legalMethods.join('、'));
            return false;
        }
        xhr.open(opt.method, opt.url);
        opt.data ? xhr.send(opt.data) : xhr.send();
        xhr.onreadystatechange = function() {
            if (xhr.status >= 200 && xhr.status < 300 || xhr.status === 304) {
                typeof opt.success === 'function' && opt.success(xhr.response);
            } else if (xhr.status >= 400 && xhr.status < 600) {
                typeof opt.error === 'function' && opt.error();
            }
        };
        xhr.onloadend = typeof opt.callback === 'function' ? opt.callback : noop;
    }
    return function (options) {
        return new Jim(options);
    };
});
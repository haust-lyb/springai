import request from "@/api/request.js"

const login = (params) => {
    return request({
        url: "/auth/login",
        method: "post",
        data: params
    })
}

const userInfo = () => {
    return request({
        url: "/auth/userinfo",
        method: "get"
    })
}

export default {
    login,
    userInfo,
}

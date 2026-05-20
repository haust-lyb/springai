import request from "@/api/request.js"

const login = (params) => {
    return request({
        url: "/auth/login",
        method: "post",
        data: params
    })
}

export default {
    login,
}
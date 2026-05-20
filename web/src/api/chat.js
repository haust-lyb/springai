import request from "@/api/request.js"

const getAssistants = (params) => {
    return request({
        url: "/assistants",
        method: "get",
        params
    })
}

export default {
    getAssistants,
}
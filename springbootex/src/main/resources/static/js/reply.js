async function get1(bno){
    const result=await axios.get(`/api/replies/list/${bno}`)
    return result.data
}

async function getList(bno,page,size,goLast){
    const result=await axios.get(`/api/replies/list/${bno}`,{params:{page,size}})
    if(goLast){
        const total=result.data.total;
        const lastPage=parseInt(Math.ceil(total/size))
        return getList(bno,lastPage,size)
    }
    return result.data
}

async function addReply(replyObj){
    const response = await axios.post(`/api/replies/`,replyObj)
    return response.data
}

async function getReply(rno){
    const reply=await axios.get(`/api/replies/${rno}`)
    return reply.data
}

async function modifyReply(replyObj){
    const response=await axios.put(`/api/replies/${replyObj.rno}`,replyObj)
    return response.data
}

async function removeReply(rno){
    const response=await axios.delete(`/api/replies/${rno}`)
    return response.data
}
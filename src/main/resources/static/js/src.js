const search = () => {
    let query = $("#search-input").val();
    if(query == ''){
        $(".search-result").hide();
    }else{
        console.log(query);
        let url = `http://localhost:9999/search/${query}`;
        fetch(url).then((response) => {
            return response.json();
        }).then((data) => {
            let text = `<div class ='list-group'>`;
            data.forEach((contact) => {
                text += `<a href = '/user/contact/${contact.cid}' class = 'list-group-item list-group-action'> ${contact.name} </a>`;
            });
            text += `</div>`;
            $(".search-result").html(text);
            $(".search-result").show();
        });
    }
}
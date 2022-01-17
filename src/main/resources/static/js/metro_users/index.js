$(function(){
    $(document).on('click', '#delete', function(event)
    {
        event.preventDefault();
        if (window.confirm("Удалить пользователя?")){
            var tr = $(this).closest('tr');
            var user_id = $(this).closest('a').attr("value");
            if (user_id == '') {
              alert("Пользователь не найден");
              return;
            }

            var user = {
                "user_id": user_id
            };

            $.ajax({
                type: "POST",
                url: "/users/delete",
                contentType: "application/json",
                data: JSON.stringify(user),
                success: function (data) {
                  console.log("success");
                  if (data.status == "Error"){
                    alert(data.data);
                    return;
                  }
                  else{
                    show_notification(data.data);
                    tr.remove();
                  }
                },
                error: function (e){
                    console.log(e);
                }
            });
        }
     });

     function show_notification(login){
        $('#notification').text("Пользователь " + login + " удален");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }

})
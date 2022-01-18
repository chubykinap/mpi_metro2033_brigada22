$(function(){

    $(document).ready(function() {
        $(function(){
          var page_size = $("#page_size").text();
          var start_page = $("#start_page").text();

          var start_index = page_size * (start_page - 1) + 1;

          var table = document.getElementsByTagName('table')[0],
          rows = table.getElementsByTagName('tr'),
          text = 'textContent' in document ? 'textContent' : 'innerText';

          var j = start_index;
          for (var i = 1, len = rows.length; i < len; i++) {
            rows[i].children[0][text] = j + rows[i].children[0][text];
            j++;
          }
        })
    });

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
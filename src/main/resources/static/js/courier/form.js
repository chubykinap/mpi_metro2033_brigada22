$(document).ready(function() {
    $(function(){
        $("#create").click(function(){
          var user = $("#user").val();
          console.log(user);

          if (user == '') {
            $( "#user_validation" ).text("Выберите пользователя");
            alert("Выберите пользователя");
            return;
          }else{
            $( "#user_validation" ).text('');
          }

          var courier = {
            "user_id": user
          };

          $.ajax({
            type: "POST",
            url: "/courier/create",
            contentType: "application/json",
            data: JSON.stringify(courier),
            success: function (data) {
              console.log("success");
              console.log(data);

               if (data.status == "Error"){
                 alert(data.data);
                 return;
               }
               else{
                show_notification(data.data);
                $("#user option[value=" + user + "]").remove();
                document.getElementById("ajax_create_courier").reset();
                console.log($("#user option").length);
                if($("#user option").length <= 1){
                    fromReset();
                }
               }
              //window.location.href = '/courier';
            },
            error: function (e){
                console.log(e);
            }
          });
        });
    });

     function show_notification(info){
        $('#notification').text("Курьер: " + info.login + " " + info.name + " " + info.surname + " создан");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }

      function fromReset() {
          var elem = document.getElementById("create_courier_form");
          elem.parentNode.removeChild(elem);
          document.getElementById("no_users_div").style.visibility = "visible";
          $('#no_users_div').append("<h4>Нет пользователей с ролью курьер</h4>");
      }

});
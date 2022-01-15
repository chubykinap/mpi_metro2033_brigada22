$(document).ready(function() {
            $(function(){
                $("#create").click(function(){
                  var user = $("#user").val();
                  console.log(user);

                  if (user == '') {
                    $( "#user_validation" ).text("Выберите пользователя");
                    return;
                  }else{
                    $( "#user_validation" ).text('');
                  }

                  var courier = {
                    "user_id": user
                  };

                  $.ajax({
                    type: "POST",
                    url: "/courier",
                    contentType: "application/json",
                    data: JSON.stringify(courier),
                    success: function (data) {
                      console.log("success");
                      console.log(data);
                      window.location.href = '/courier';
                    }
                  });
                });
            })
        });
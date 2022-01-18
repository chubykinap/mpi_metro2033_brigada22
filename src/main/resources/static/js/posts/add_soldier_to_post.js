$(document).ready(function() {
    $(function(){

        $("#change").click(function(){

          check = false;

          $('#ajax_change_post *').filter(':input').each(function(){
              if($(this).val() == ''){
                check = true;
              }
          });

          if (check == true){
            alert("Выберите солдата!");
            return;
          }

          var add_soldier_to_post = {
            "post_id":$("#post_id").val(),
            "soldier_id":$("#soldier").val()
          };

          $.ajax({
            type: "POST",
            url: "/posts/add_soldier_to_post",
            contentType: "application/json",
            data: JSON.stringify(add_soldier_to_post),
            // dataType: "dataType",
            success: function (data) {
              console.log("success");
                if (data.status == "Error"){
                  alert(data.data);
                  return;
                }
                else{
                  show_notification(data.data);
                  $("#soldier option[value=" + $("#soldier").val() + "]").remove();
                  console.log($("#soldier option").length);
                  if($("#soldier option").length <= 1){
                      fromReset();
                  }
                }
              //window.location.href = '/posts';
            },
             error: function (e){
                 console.log(e);
             }
          });
        });
    })

     function show_notification(info){
        $('#notification').text("Солдат:  " + info.name + " " + info.location + " добавлен на пост");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }

      function fromReset() {
          document.getElementById("ajax_change_post").reset();
          var elem = document.getElementById("create_div");
          elem.parentNode.removeChild(elem);
          document.getElementById("no_users_div").style.visibility = "visible";
          $('#no_users_div').append("<h4>Нет свободных солдатов</h4>");
      }

});
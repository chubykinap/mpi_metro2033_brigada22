$(document).ready(function() {

    $(document).ready(function() {
        $(function(){

          var table = document.getElementsByTagName('table')[0],
          rows = table.getElementsByTagName('tr'),
          text = 'textContent' in document ? 'textContent' : 'innerText';

          for (var i = 1, len = rows.length; i < len; i++) {
            rows[i].children[0][text] = i + rows[i].children[0][text];
          }
        })
    });

    $(document).on('click', '#delete', function(event)
    {
        event.preventDefault();
        if (window.confirm("Удалить солдтата из поста?")){
            var tr = $(this).closest('tr');
            var soldier_id = $(this).closest('a').attr("value");
            if (soldier_id == '') {
              alert("Солдат не найден!");
              return;
            }

            var soldier = {
                "soldier_id": soldier_id
            };

            $.ajax({
                type: "POST",
                url: "/posts/remove_from_post",
                contentType: "application/json",
                data: JSON.stringify(soldier),
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

     function show_notification(info){
        $('#notification').text("Солдат: " + info.name + " " + info.surname + " удален");
        $('#notification').show();
        $('#notification').delay(7000).hide(0);
     }
});
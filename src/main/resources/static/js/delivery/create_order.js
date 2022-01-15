function getTableData(id) {
    var $t_data = [];
    $("#t_items tbody tr").each(function(i) {
        var $c_data = [];
        var $item_name = $(this).find('#t_name').text();
        var $item_quantity = $(this).find('#t_quantity').val();
        if ($item_quantity > 0) {
            $c_data.push($item_name);
            $c_data.push($item_quantity);
            $t_data.push($c_data);
        }
    });
    return $t_data;
}

$(document).ready(function() {
    $(function() {
        $("#create").click(function() {
            check = false;

            var courier = $("#courier").val();
            if (courier == '') {
                $( "#courier_validation" ).text("Выберите курьера");
                check = true;
            }else{
                $( "#courier_validation" ).text("");
            }

            var station = $("#station").val();
            if (station == '') {
                $( "#station_validation" ).text("Выберите станцию");
                check = true;
            }else{
                $( "#station_validation" ).text("");
            }

            var date = $("#date_select").val();
            if (date == '') {
                $( "#date_validation" ).text("Выберите дату доставки");
                check = true;
            }else{
                $( "#date_validation" ).text("");
            }

            var items = getTableData('t_items');
            console.log(items);
            if (items.length < 1) {
                $( "#item_validation" ).text("Не добавлены предметы для заказа");
                return;
            }else{
                $( "#item_validation" ).text("");
            }

            if (check == true){
                alert("Не все данные заполнены!");
                return;
            }

            var order = {
                "courier_id":$("#courier").val(),
                "station":$("#station").val(),
                "direction":$("#direction").text(),
                "date":$('#date_select').val(),
                "items":items
            };

            $.ajax({
                type: "POST",
                url: "/delivery",
                contentType: "application/json",
                data: JSON.stringify(order),
                success: function (data) {
                  console.log("success");
                  console.log(data);
                  window.location.href = '/delivery';
                }
            });
        });
    })
});
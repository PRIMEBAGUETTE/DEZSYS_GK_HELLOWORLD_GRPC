import com.example.grpc.DataWarehouseProto;
import com.example.grpc.DataWarehouseServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DataWarehouseClient {

    public static void main(String[] args) {

        String warehouseID = args.length > 0 ? args[0] : "1";
        String warehouseName = args.length > 1 ? args[1] : "Daniel Warehouse";
        String warehouseAddress = args.length > 2 ? args[2] : "67 street";
        String warehousePostalCode = args.length > 3 ? args[3] : "1210";
        String warehouseCity = args.length > 4 ? args[4] : "Wien";
        String warehouseCountry = args.length > 5 ? args[5] : "Austria";
        String timestamp = args.length > 6 ? args[6] : "2025-12-02T12:00:00Z";

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        DataWarehouseServiceGrpc.DataWarehouseServiceBlockingStub stub =
                DataWarehouseServiceGrpc.newBlockingStub(channel);

        DataWarehouseProto.DataWarehouseRecord record = DataWarehouseProto.DataWarehouseRecord.newBuilder()
                .setWarehouseID(warehouseID)
                .setWarehouseName(warehouseName)
                .setWarehouseAddress(warehouseAddress)
                .setWarehousePostalCode(warehousePostalCode)
                .setWarehouseCity(warehouseCity)
                .setWarehouseCountry(warehouseCountry)
                .setTimestamp(timestamp)
                .build();

        DataWarehouseProto.RecordResponse response = stub.sendRecord(record);

        System.out.println(response.getStatus());

        channel.shutdown();
    }
}

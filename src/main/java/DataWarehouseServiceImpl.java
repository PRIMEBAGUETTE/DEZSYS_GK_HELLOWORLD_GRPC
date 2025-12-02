import com.example.grpc.DataWarehouseProto;
import com.example.grpc.DataWarehouseServiceGrpc;
import io.grpc.stub.StreamObserver;

public class DataWarehouseServiceImpl extends DataWarehouseServiceGrpc.DataWarehouseServiceImplBase {

    @Override
    public void sendRecord(DataWarehouseProto.DataWarehouseRecord request,
                           StreamObserver<DataWarehouseProto.RecordResponse> responseObserver) {

        System.out.println("Received DataWarehouse record:");
        System.out.println("ID: " + request.getWarehouseID());
        System.out.println("Name: " + request.getWarehouseName());
        System.out.println("Address: " + request.getWarehouseAddress());
        System.out.println("Postal Code: " + request.getWarehousePostalCode());
        System.out.println("City: " + request.getWarehouseCity());
        System.out.println("Country: " + request.getWarehouseCountry());
        System.out.println("Timestamp: " + request.getTimestamp());

        String status = "Record received: " + request.getWarehouseName();
        DataWarehouseProto.RecordResponse response = DataWarehouseProto.RecordResponse.newBuilder()
                .setStatus(status)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

# Middleware Engineering "DEZSYS_GK_HELLOWORLD_GRPC"

## Dokumentation
1. Protofile Configuration

For GK, I added:
option java_package = "com.example.grpc";
option java_outer_classname = "hello";


2. Generate Files

For Java with Gradle: .\gradlew generateProto
For Python: python -m grpc_tools.protoc -I=src/main/proto --python_out=src/main/resources --grpc_python_out=src/main/resources src/main/proto/datawarehouse.proto
This allows you to easily generate code for different programming languages.


3. Ek adaptations

First we have to change the Protofile, so we dont send a string with hello world, and instead send a record with the data a warehouse should have:
```
syntax = "proto3";

option java_package = "com.example.grpc";
option java_outer_classname = "DataWarehouseProto";

service DataWarehouseService {
  rpc sendRecord(DataWarehouseRecord) returns (RecordResponse);
}

message DataWarehouseRecord {
  string warehouseID = 1;
  string warehouseName = 2;
  string warehouseAddress = 3;
  string warehousePostalCode = 4;
  string warehouseCity = 5;
  string warehouseCountry = 6;
  string timestamp = 7;
}

message RecordResponse {
  string status = 1;
}

```


After that we need to make adaptations to the service and client, the server can stay as it is, because the changes we need are already in the auto generated code.
```java
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

```

```java
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


```


4. Python client
For that we need the python code generated from the .proto file, so execute the command in 1.
With them we can implement the client like in java, using args parameters and we can use it in the same way
```python
import sys
sys.path.append('src/main/resources')  # Add resources folder to Python path

import grpc
import datawarehouse_pb2
import datawarehouse_pb2_grpc

def run():
    # Connect to Java gRPC server
    channel = grpc.insecure_channel('localhost:50051')
    stub = datawarehouse_pb2_grpc.DataWarehouseServiceStub(channel)

    # Create a record
    record = datawarehouse_pb2.DataWarehouseRecord(
        warehouseID="1",
        warehouseName="Wimma",
        warehouseAddress="WImmerstraße",
        warehousePostalCode="12345",
        warehouseCity="Wien",
        warehouseCountry="Austria",
        timestamp="2025-12-02T12:00:00Z"
    )

    # Call gRPC method
    response = stub.sendRecord(record)
    print("Server response:", response.status)

if __name__ == "__main__":
    run()

```

## Fragen

• What is gRPC and why does it work accross languages and platforms?

Google Remote Procedure Call is a framework that allows methods in one program to be executed by another program. You always create a .proto file that defines methods for RPC, which can be generated in different languages.

• Describe the RPC life cycle starting with the RPC client?

Client creates stub and calls a method on the server; the message is serialized and sent

The server receives and deserializes the message, and executes the method

Then a response is sent back in the same way

• Describe the workflow of Protocol Buffers?

It is responsible for serializing and deserializing messages. The methods for this are generated from the .proto file.

• What are the benefits of using protocol buffers?

Efficient, fast, and low data usage. Also language-independent.

• When is the use of protocol not recommended?

When data should be human-readable; in that case, JSON would be better.

• List 3 different data types that can be used with protocol buffers?

Int32
String
Bool

## Implementierung

Start HelloWorldServer (Java)  
`gradle clean build`  
`gradle runServer`

Start HelloWorldClient (Java)  
`gradle runClient`
  

-------------------------------- Python 

Add grpcio packages  
`pip3 install grpcio grpcio-tools`  

Compile .proto file  
`python3 -m grpc_tools.protoc -I src/main/proto  
  --python_out=src/main/resources  
  --grpc_python_out=src/main/resources  
  src/main/proto/hello.proto`  

Start HelloWorldClient (Python)  
`python3 src/main/resources/helloWorldClient.py`  

## Quellen

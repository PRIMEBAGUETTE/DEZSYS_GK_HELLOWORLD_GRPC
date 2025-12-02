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

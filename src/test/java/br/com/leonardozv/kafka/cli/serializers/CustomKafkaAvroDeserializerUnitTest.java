//package br.com.leonardozv.kafka.cli.serializers;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyBoolean;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CustomKafkaAvroDeserializerUnitTest {
//
//    @Mock
//    private CustomKafkaAvroDeserializer customKafkaAvroDeserializer;
//
//    @Test
//    void whenConfigure_thenCallConfigureMethod() {
//
//        doNothing().when(this.customKafkaAvroDeserializer).configure(any(), anyBoolean());
//
//        this.customKafkaAvroDeserializer.configure(null, true);
//
//        verify(this.customKafkaAvroDeserializer, times(1)).configure(null, true);
//
//    }
//
//    @Test
//    void whenDeserialize_thenCallDeserializeMethod() {
//
//        when(this.customKafkaAvroDeserializer.deserialize(any(), any())).thenReturn(null);
//
//        Object retorno = this.customKafkaAvroDeserializer.deserialize(null, null);
//
//        verify(this.customKafkaAvroDeserializer, times(1)).deserialize(any(), any());
//
//        assertNotNull(retorno);
//
//    }
//
//}

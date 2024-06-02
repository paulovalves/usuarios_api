Sistema simples para cadastro de usuários

## Como rodar

### Terminal

Na pasta do projeto executar os comandos:
      
    mvn clean install
    mvn spring-boot:run

para acessar a documentação OpenAPI

    http://localhost:8080/swagger-ui/index.html

## Tecnologias

    Java 17
    Spring Boot 3.3.0
    OpenAPI
    H2

## Explicações

Os métodos de teste cobrem uma variedade de cenários que são críticos para a funcionalidade e segurança do serviço de usuários. Eles garantem que a criação de usuários, a busca por ID e a listagem de todos os usuários funcionam corretamente, lidando com casos de sucesso, falha de validação, exceções internas do servidor e controle de acesso. Isso proporciona uma cobertura abrangente para as funcionalidades principais da aplicação.

A escolha da tecnologia de frontend depende das necessidades específicas do projeto, da experiência da equipe de desenvolvimento e dos requisitos de manutenção e escalabilidade. As principais opções atualmente são tecnologias SPA como React, Angular e Vue.js, cada uma com suas particularidades:

* React.js (Next.js): Flexível e possui um vasto ecossistema.

* Angular: Framework completo e com tipagem estática usando TypeScript.

* Vue.js (Nuxt): Ótimo pela simplicidade, fácil adoção e boa documentação.

Alguns contras para a utilização de tecnologias SPA são o tempo de carregamento inicial lento devido à quantidade de JavaScript carregado no início, a curva de aprendizado de frameworks como Angular, o gerenciamento de estado e, por fim, a segurança pode ser um problema, uma vez que a maior parte da aplicação é executada do lado do cliente.

* Spring MVC: Permite uma integração fluida entre o frontend e o backend, facilitando a manipulação de dados e lógica de negócios. Também compartilha os mesmos modelos, reduzindo a redundância e o mapeamento de dados entre o frontend e o backend.

Porém, pode ser difícil de gerenciar caso a aplicação cresça em complexidade e possui menor interatividade, sendo necessário o carregamento total da página caso alguma operação seja feita.